package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.db.ClassSectionDao
import org.ionproject.android.common.db.ClassSummaryDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSection
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
    private val workerManagerFacade: WorkerManagerFacade
) {

    suspend fun getClassSection(classSummary: ClassSummary): ClassSection {
        return ionWebAPI
            .getFromURI(classSummary.detailsUri, SirenEntity::class.java)
            .toClassSection()
    }

    //Adds the calendar term to the classes URI
    private fun URI.fromCalendarTerm(calendarTerm: CalendarTerm): URI {
        val newUri = "${toString()}/${calendarTerm.name}"
        return URI(newUri)
    }

    suspend fun getClassesFromCourse(
        course: Course,
        calendarTerm: CalendarTerm,
        onResult: (List<ClassSummary>) -> Unit
    ) = coroutineScope {
        var classes = classSummaryDao.getClassSummariesByCourseAndCalendarTerm(
            course.acronym,
            calendarTerm.name
        )
        if (classes.count() == 0 && course.classesUri != null) {
            classes = ionWebAPI.getFromURI(
                course.classesUri.fromCalendarTerm(calendarTerm),
                SirenEntity::class.java
            ).toClassSummaryList()
            if (!classes.isEmpty())
                launch {
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
        onResult(classes)
    }

    /**
     * Runs the coroutine with the [Dispatchers.IO]
     * which is optimized to perform disk or network I/O outside of the main thread.
     *
     * @param classSection is the classSection to add to the db
     */
    suspend fun addClassSectionToDb(classSection: ClassSection) {
        withContext(Dispatchers.IO) {
            classSectionDao.insertClassSection(classSection)
        }
    }

    /**
     * Runs the coroutine with the [Dispatchers.IO]
     * which is optimized to perform disk or network I/O outside of the main thread.
     *
     * @param classSection is the classSection to remove from the db
     */
    suspend fun removeClassSectionFromDb(classSection: ClassSection) {
        withContext(Dispatchers.IO) {
            classSectionDao.deleteClassSection(classSection)
        }
    }
}
