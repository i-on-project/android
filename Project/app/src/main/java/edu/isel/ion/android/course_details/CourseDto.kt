package edu.isel.ion.android.course_details

import edu.isel.ion.android.common.EmbeddedEntity
import edu.isel.ion.android.common.SirenEntity
import edu.isel.ion.android.common.model.Course


/*
    Represents the properties of the course representation in siren
 */
data class CourseProperties (
    val acronym : String,
    val name : String)

/**
   Converts from a course [SirenEntity] to [Course]
 */
fun SirenEntity<CourseProperties>.toCourse() : Course {

    val classesLinks = (this.entities!![0] as EmbeddedEntity<*>).links
    val classesLink = classesLinks!![0].href

    val eventsLinks = (this.entities[1] as EmbeddedEntity<*>).links
    val eventlink = eventsLinks!![1].href

    return Course(
        this.properties!!.name,
        this.properties.acronym,
        classesLink,
        eventlink
    )
}




