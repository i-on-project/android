package org.ionproject.android.course_details

import com.fasterxml.jackson.annotation.JsonCreator
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

/**
 *   Represents the properties of the course representation in siren
 */
data class CourseProperties @JsonCreator constructor(
    val acronym: String,
    val name: String
)

/**
 *  Converts from a course [SirenEntity] to [Course]
 */
fun SirenEntity<CourseProperties>.toCourse(): Course {
    val classesLink: URI? = (entities?.first() as EmbeddedEntity<*>).links?.first()?.href
    val eventsLink: URI? = (entities.last() as EmbeddedEntity<*>).links?.first()?.href
    properties as LinkedHashMap<String, String>

    return Course(
        acronym = properties["acronym"] ?: "err",
        name = properties["name"] ?: "error",
        classesUri = classesLink,
        eventsUri = eventsLink
    )
}
