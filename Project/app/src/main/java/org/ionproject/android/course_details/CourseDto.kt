package org.ionproject.android.course_details

import org.ionproject.android.common.dto.EmbeddedEntity
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.dto.findByRel
import org.ionproject.android.common.model.ClassCollection
import org.ionproject.android.common.model.ClassCollectionFields
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import java.net.URI

/**
 *  Converts from a [SirenEntity] to [Course]
 */
fun SirenEntity.toCourse(): Course {
    val id = properties?.get("id")
    val acronym = properties?.get("acronym")
    val name = properties?.get("name")
    val selfUri = links?.first()?.href
    val classesLink: URI? = (entities?.first() as EmbeddedEntity).links?.first()?.href
    val eventsLink: URI? = (entities.last() as EmbeddedEntity).links?.first()?.href

    if (id != null && properties != null && acronym != null && name != null && selfUri != null) {
        //Using double bang operator because we are sure this properties cannot be null here
        return Course(
            id = id.toInt(),
            acronym = acronym,
            name = name,
            classesUri = classesLink,
            eventsUri = eventsLink,
            selfUri = selfUri
        )
    }
    throw MappingFromSirenException("Cannot convert $this to Course")
}

/**
 *  Converts from a [SirenEntity] to [ClassCollection]
 */
fun SirenEntity.toClassCollection(): ClassCollection {
    val classesSummary = mutableListOf<ClassSummary>()

    val courseId = properties?.get("courseId")?.toInt()
    val courseAcronym = properties?.get("courseAcr")
    val calendarTerm = properties?.get("calendarTerm")
    var calendarUri: URI? = null
    val selfUri = links?.findByRel("self")

    if (courseAcronym != null && calendarTerm != null && selfUri != null && courseId != null) {
        entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity)

            embeddedEntity.clazz?.apply {
                //There is an event sub-entity which is not from the class "class", which we must exclude
                if (embeddedEntity.clazz.containsAll(listOf("class", "section"))) {
                    val id = embeddedEntity.properties?.get("id")
                    val detailsUri: URI? = embeddedEntity.links?.findByRel("self")

                    if (id != null && detailsUri != null)
                        classesSummary.add(
                            ClassSummary(id, courseAcronym, calendarTerm, detailsUri)
                        )

                } else if (embeddedEntity.clazz.contains("calendar")) {
                    calendarUri = embeddedEntity.links?.findByRel("self")
                }

            } ?: throw MappingFromSirenException("Cannot convert $this to List of ClassSummary")
        }
        return ClassCollection(
            ClassCollectionFields(
                courseId,
                courseAcronym,
                calendarTerm,
                calendarUri,
                selfUri
            ),
            classesSummary
        )
    }
    throw MappingFromSirenException("Cannot convert $this to List of ClassSummary")
}

