package org.ionproject.android.common.repositories

import org.ionproject.android.class_section.ClassListProperties
import org.ionproject.android.class_section.ClassSectionProperties
import org.ionproject.android.class_section.toClassSection
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.siren.EmbeddedEntity

class ClassesRepository(private val ionWebAPI: IIonWebAPI) {

    suspend fun getClassSection(classSummary: ClassSummary): ClassSection {
        return ionWebAPI
            .getFromURI<ClassSectionProperties>(classSummary.detailsUri)
            .toClassSection()
    }

    suspend fun getClassesFromCourse(course: Course): List<ClassSummary> {
        val classesSummary = mutableListOf<ClassSummary>()

        if (course.classesUri != null) {
            ionWebAPI.getFromURI<ClassListProperties>(course.classesUri).entities?.forEach {
                val embeddedEntity = (it as EmbeddedEntity<LinkedHashMap<String, String>>)

                val clazz = embeddedEntity.clazz?.first()
                val id = embeddedEntity.properties?.get("id")
                val uri = embeddedEntity.links?.first()?.href

                if (clazz == "class" && id != null && uri != null)
                    classesSummary.add(ClassSummary(id = id, detailsUri = uri))
            }
        }
        return classesSummary
    }

}
