package org.ionproject.android.course_details

import org.ionproject.android.common.dto.*
import org.ionproject.android.common.model.*
import java.net.URI

/**
 *  Converts from a [SirenEntity] to [Course]
 */
fun SirenEntity.toCourse(): Course {
    val id = properties?.get("id") as? Int
    val acronym = properties?.get("acronym") as? String
    val name = properties?.get("name") as? String
    val selfUri = links?.findByRel("self")
    val classesLink: URI? =
        entities?.findEmbeddedEntityByRel("/rel/class")?.links?.findByRel("self")

    if (id != null && properties != null && acronym != null && selfUri != null) {
        //Using double bang operator because we are sure this properties cannot be null here
        return Course(
            id = id,
            acronym = acronym,
            name = name,
            classesUri = classesLink,
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

    val courseId = properties?.get("courseId") as? Int
    val courseAcronym = properties?.get("courseAcr") as? String
    val calendarTerm = properties?.get("calendarTerm") as? String
    var calendarUri: URI? = null
    val selfUri = links?.findByRel("self")

    if (courseAcronym != null && calendarTerm != null && selfUri != null && courseId != null) {
        entities?.forEach {
            val embeddedEntity = (it as? EmbeddedEntity)

            embeddedEntity?.clazz?.apply {
                if (embeddedEntity.clazz.containsAll(listOf("class", "section"))) {
                    val id = embeddedEntity.properties?.get("id") as? String
                    val detailsUri: URI? = embeddedEntity.links?.findByRel("self")

                    if (id != null && detailsUri != null)
                        classesSummary.add(
                            ClassSummary(id, courseAcronym, calendarTerm, detailsUri)
                        )

                } else if (embeddedEntity.clazz.contains("calendar")) {
                    calendarUri = embeddedEntity.links?.findByRel("self")
                }
            }
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

/**
 *  Converts from a [SirenEntity] to a list of [Classes]
 */
fun SirenEntity.toClasses(): List<Classes> {
    val classesList = mutableListOf<Classes>()

    val courseId = properties?.get("cid") as? Int
    val selfUriPath =
        links?.findByRel("self")?.path // This is required because it might be an hreftemplate

    if (courseId != null && selfUriPath != null) {
        entities?.findEmbeddedEntitiesByRel("item")?.forEach {
            val calendarTerm = it.properties?.get("calendarTerm") as? String
            val detailsUri: URI? = it.links?.findByRel("self")

            if (calendarTerm != null && detailsUri != null)
                classesList.add(
                    Classes(
                        courseId,
                        calendarTerm,
                        detailsUri,
                        URI(selfUriPath)
                    )
                )
        }
        return classesList
    }
    throw MappingFromSirenException("Cannot convert $this to List of Classes")
}

