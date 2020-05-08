package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.db.ClassSectionDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.course_details.toClassSummaryList
import java.net.URI

class ClassesRepository(
    private val ionWebAPI: IIonWebAPI,
    private val classSectionDao: ClassSectionDao
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
        calendarTerm: CalendarTerm
    ): List<ClassSummary> {
        var classes = emptyList<ClassSummary>()

        if (course.classesUri != null) {
            classes = ionWebAPI.getFromURI(
                course.classesUri.fromCalendarTerm(calendarTerm),
                SirenEntity::class.java
            ).toClassSummaryList()
        }
        //Some courses may not have classes in a certain term, in those cases we return an emptyList
        return classes
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
