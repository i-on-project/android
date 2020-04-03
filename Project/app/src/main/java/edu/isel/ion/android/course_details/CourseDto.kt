package edu.isel.ion.android.course_details

import edu.isel.ion.android.common.EmbeddedEntity
import edu.isel.ion.android.common.SirenEntity
import edu.isel.ion.android.common.model.Course
import java.net.URI

/**
 *   Represents the properties of the course representation in siren
 */
data class CourseProperties(
    val acronym: String,
    val name: String
)

/**
 *  Converts from a course [SirenEntity] to [Course]
 */
fun SirenEntity<CourseProperties>.toCourse(): Course {
    val classesLink: URI? = (entities!!.first() as EmbeddedEntity<*>).links?.first()?.href
    val eventsLink: URI? = (entities.last() as EmbeddedEntity<*>).links?.first()?.href

    return Course(
        acronym = properties!!.acronym,
        name = properties.name,
        classesUri = classesLink,
        eventsUri = eventsLink
    )
}




