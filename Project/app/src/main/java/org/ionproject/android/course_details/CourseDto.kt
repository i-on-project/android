package org.ionproject.android.course_details

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import java.net.URI

/**
 *  Converts from a [SirenEntity] to [Course]
 */
fun SirenEntity.toCourse(): Course {
    val classesLink: URI? = (entities?.first() as EmbeddedEntity).links?.first()?.href
    val eventsLink: URI? = (entities.last() as EmbeddedEntity).links?.first()?.href

    val acronym = properties?.get("acronym")
    val name = properties?.get("name")

    if (properties != null && acronym != null && name != null) {
        //Using double bang operator because we are sure this properties cannot be null here
        return Course(
            acronym = acronym,
            name = name,
            classesUri = classesLink,
            eventsUri = eventsLink
        )
    }
    throw MappingFromSirenException("Cannot convert $this to Course")
}

/**
 *  Converts from a [SirenEntity] to [ClassSummary]
 */
fun SirenEntity.toClassSummaryList(): List<ClassSummary> {
    val classesSummary = mutableListOf<ClassSummary>()

    val course = properties?.get("courseAcr")
    val calendarTerm = properties?.get("calendarTerm")

    if (course != null && calendarTerm != null) {
        entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity)

            //There is an event sub-entity which is not from the class "class", which we must exclude
            if (embeddedEntity.clazz?.first() == "class") {
                val name = embeddedEntity.properties?.get("id")
                val detailsUri: URI? = embeddedEntity.links?.first()?.href

                if (name != null && detailsUri != null)
                    classesSummary.add(ClassSummary(name, course, calendarTerm, detailsUri))
                else
                    throw MappingFromSirenException(
                        "Cannot convert $this to List of ClassSummary"
                    )
            }
        }
        return classesSummary
    }
    throw MappingFromSirenException("Cannot convert $this to List of ClassSummary")
}

