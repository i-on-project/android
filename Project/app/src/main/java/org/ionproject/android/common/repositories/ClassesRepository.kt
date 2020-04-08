package org.ionproject.android.common.repositories

import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.course_details.toClassSummaryList

class ClassesRepository(private val ionWebAPI: IIonWebAPI) {

    suspend fun getClassSection(classSummary: ClassSummary): ClassSection {
        return ionWebAPI
            .getFromURI(classSummary.detailsUri)
            .toClassSection()
    }

    suspend fun getClassesFromCourse(course: Course): List<ClassSummary> {
        if (course.classesUri != null) {
            return ionWebAPI
                .getFromURI(course.classesUri)
                .toClassSummaryList()
        }
        //Some courses may not have classes in a certain term, in those cases we return an emptyList
        return emptyList()
    }
}
