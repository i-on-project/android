package edu.isel.ion.android.course_details

import edu.isel.ion.android.common.EmbeddedEntity
import edu.isel.ion.android.common.SirenEntity
import com.fasterxml.jackson.annotation.JsonCreator
import edu.isel.ion.android.common.SubEntity
import edu.isel.ion.android.common.model.Course
import edu.isel.ion.android.common.model.CourseSummary
import java.net.URI

/**
 *   Represents the properties of the course representation in siren
 */
data class CourseProperties @JsonCreator constructor(
    val acronym: String,
    val name: String
)
/**
 *   Represents the properties of the course summary representation in siren
 */
data class CourseSummaryProperties @JsonCreator constructor(
    val acronym: String
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

/**
 *  Converts from a course summary [EmbeddedEntity] to [CourseSummary]
 */
fun EmbeddedEntity<CourseSummaryProperties>.toCourseSummary() : CourseSummary {
    val detailsLink : URI? = links!!.first().href

    return CourseSummary(
        acronym = properties!!.acronym,
        detailsUri = detailsLink!!
    )
}






