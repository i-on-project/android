package org.ionproject.android.course_details

import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.MappingFromSirenException
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

/**
 *  Converts from a [SirenEntity] to [Course]
 */
fun SirenEntity.toCourse(): Course {
    val classesLink: URI? = (entities?.first() as EmbeddedEntity).links?.first()?.href
    val eventsLink: URI? = (entities.last() as EmbeddedEntity).links?.first()?.href

    if (properties != null && properties["acronym"] != null && properties["name"] != null) {
        //Using double bang operator because we are sure this properties cannot be null here
        return Course(
            acronym = properties["acronym"]!!,
            name = properties["name"]!!,
            classesUri = classesLink,
            eventsUri = eventsLink
        )
    }
    throw MappingFromSirenException("Cannot convert ${this} to Course")
}

/**
 *  Converts from a [SirenEntity] to [ClassSummary]
 */
fun SirenEntity.toClassSummaryList(): List<ClassSummary> {
    val classesSummary = mutableListOf<ClassSummary>()

    entities?.forEach {
        val embeddedEntity = (it as EmbeddedEntity)

        val clazz = embeddedEntity.clazz?.first()
        val id = embeddedEntity.properties?.get("id")
        val uri = embeddedEntity.links?.first()?.href

        //There is an event sub-entity which is not from the class "class", which we must exclude
        if (clazz == "class") {
            if (id != null && uri != null)
                classesSummary.add(ClassSummary(id = id, detailsUri = uri))
            else
                throw MappingFromSirenException("Cannot convert ${this} to List of ClassSummary")
        }

    }
    return classesSummary
}

