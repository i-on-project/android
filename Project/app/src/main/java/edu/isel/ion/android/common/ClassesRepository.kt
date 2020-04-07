package edu.isel.ion.android.common

import edu.isel.ion.android.class_section.ClassListProperties
import edu.isel.ion.android.class_section.ClassSectionProperties
import edu.isel.ion.android.class_section.toClassSection
import edu.isel.ion.android.common.ionwebapi.IIonWebAPI
import edu.isel.ion.android.common.model.ClassSection
import edu.isel.ion.android.common.model.ClassSummary
import edu.isel.ion.android.common.model.Course
import java.net.URI

class ClassesRepository(private val ionWebAPI: IIonWebAPI) {

    suspend fun getClassSection(classSummary: ClassSummary): ClassSection {
        return ionWebAPI.getFromURI<ClassSectionProperties>(classSummary.detailsUri).toClassSection()
    }

    suspend fun getClassesFromCourse(course : Course) : List<ClassSummary> {
        return ionWebAPI.getFromURI<ClassListProperties>(course.classesUri!!).entities!!.mapNotNull {
            val embeddedEntity = (it as EmbeddedEntity<LinkedHashMap<String,String>>)
            if(embeddedEntity.clazz!!.first() == "class")
                return@mapNotNull ClassSummary(
                    embeddedEntity.properties!!["id"]!!,
                    embeddedEntity.links!!.first().href
                )
            null
        }
    }



}