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
fun SirenEntity.toCourse(term: Int): Course {
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
            year = getYear(term),
            term = term,
            classesUri = classesLink,
            eventsUri = eventsLink,
            selfUri = selfUri
        )
    }
    throw MappingFromSirenException("Cannot convert $this to Course")
}

/**
 *  Converts from a [SirenEntity] to [ClassSummary]
 */
fun SirenEntity.toClassSummaryList(): List<ClassSummary> {
    val classesSummary = mutableListOf<ClassSummary>()

    val courseAcronym = properties?.get("courseAcr")
    val calendarTerm = properties?.get("calendarTerm")
    val selfUri = links?.first()?.href

    if (courseAcronym != null && calendarTerm != null && selfUri != null) {
        entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity)

            //There is an event sub-entity which is not from the class "class", which we must exclude
            if (embeddedEntity.clazz?.first() == "class") {
                val id = embeddedEntity.properties?.get("id")
                val detailsUri: URI? = embeddedEntity.links?.first()?.href

                if (id != null && detailsUri != null)
                    classesSummary.add(
                        ClassSummary(
                            id,
                            courseAcronym,
                            calendarTerm,
                            detailsUri,
                            selfUri
                        )
                    )
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

/**
 * Returns the year in which the course is taught
 *
 * @param term - the semester term in which the course is taught
 */
private fun getYear(term: Int): Int =
    when(term) {
        1, 2 -> 1
        3, 4 -> 2
        else -> 3
    }

