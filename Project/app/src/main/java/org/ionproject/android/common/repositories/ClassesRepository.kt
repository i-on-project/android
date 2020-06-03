package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.db.ClassSectionDao
import org.ionproject.android.common.db.ClassSummaryDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.course_details.toClassSummaryList
import java.net.URI

class ClassesRepository(
    private val ionWebAPI: IIonWebAPI,
    private val classSectionDao: ClassSectionDao,
    private val classSummaryDao: ClassSummaryDao,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getClassSection(classSummary: ClassSummary) =
        withContext(dispatcher) {
            var classSection = classSectionDao.getClassSectionByIdAndCourseAndCalendarTerm(
                classSummary.id,
                classSummary.courseAcronym,
                classSummary.calendarTerm
            )
            if (classSection == null) {
                classSection =
                    ionWebAPI.getFromURI(classSummary.detailsUri, SirenEntity::class.java)
                        .toClassSection()
                val workerId = workerManagerFacade.enqueueWorkForClassSection(
                    classSection,
                    WorkImportance.VERY_IMPORTANT
                )
                classSection.workerId = workerId
                classSectionDao.insertClassSection(classSection)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(classSection)
            }
            classSection
        }

    //Adds the calendar term to the classes URI
    private fun URI.fromCalendarTerm(calendarTerm: CalendarTerm): URI {
        val newUri = "${toString()}/${calendarTerm.name}"
        return URI(newUri)
    }

    suspend fun getClassesFromCourse(
        course: Course,
        calendarTerm: CalendarTerm
    ) = withContext(dispatcher) {
        var classes = classSummaryDao.getClassSummariesByCourseAndCalendarTerm(
            course.acronym,
            calendarTerm.name
        )
        if (classes.count() == 0 && course.classesUri != null) {
            classes = ionWebAPI.getFromURI(
                course.classesUri.fromCalendarTerm(calendarTerm),
                SirenEntity::class.java
            ).toClassSummaryList()
            if (!classes.isEmpty()) {
                val workerId = workerManagerFacade.enqueueWorkForClassSummaries(
                    classes,
                    WorkImportance.IMPORTANT
                )
                for (classSummary in classes) {
                    classSummary.workerId = workerId
                }
                classSummaryDao.insertClassSummaries(classes)
            }
        } else {
            workerManagerFacade.resetWorkerJobsByCacheable(classes[0])
        }
        //Some courses may not have classes in a certain term, in those cases we return an emptyList
        classes
    }
}
