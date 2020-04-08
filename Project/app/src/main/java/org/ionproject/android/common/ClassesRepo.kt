package org.ionproject.android.common

import org.ionproject.android.class_section.ClassListProperties
import org.ionproject.android.class_section.ClassSectionProperties
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course

class ClassesRepository(private val ionWebAPI: IIonWebAPI) {

    suspend fun getClassSection(classSummary: ClassSummary): ClassSection {
        return ionWebAPI
            .getFromURI<ClassSectionProperties>(classSummary.detailsUri)
            .toClassSection()
    }

    suspend fun getClassesFromCourse(course: Course): List<ClassSummary> {
        return ionWebAPI.getFromURI<ClassListProperties>(course.classesUri!!).entities!!.mapNotNull {
            val embeddedEntity = (it as EmbeddedEntity<LinkedHashMap<String, String>>)
            if (embeddedEntity.clazz!!.first() == "class")
                return@mapNotNull ClassSummary(
                    embeddedEntity.properties!!["id"]!!,
                    embeddedEntity.links!!.first().href
                )
            null
        }
    }


}