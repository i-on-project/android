package edu.isel.ion.android.common

import edu.isel.ion.android.common.ionwebapi.IIonWebAPI
import edu.isel.ion.android.common.model.Course
import edu.isel.ion.android.common.model.CourseSummary
import edu.isel.ion.android.course_details.CourseProperties
import edu.isel.ion.android.course_details.toCourse
import java.net.URI

/*
    This type represents a course repository, it should request
    from the API, and map the result to the app model.
 */
class CourseRepository(private val ionWebAPI: IIonWebAPI) {

    /**
    Performs a get request to the i-on API to obtain all the courses,
    and maps from [SirenEntity] to [Course].

     TODO Should receive the curricular term and get the courses from that term
        instead of using an hardcoded URI. This should also go to the DB instead of
        the WebAPI all the time, so that the app works in offline mode.
     */
    suspend fun getAllCourses() : List<CourseSummary> {
        val uri = URI("/v0/courses")
        return ionWebAPI.getFromURI<Any>(uri).entities!!.map {
            val embeddedEntity = (it as EmbeddedEntity<LinkedHashMap<String,String>>)
            CourseSummary(
                embeddedEntity.properties!!["acronym"]!!,
                embeddedEntity.links!!.first().href
            )
        }
    }

    /**
     * @param courseSummary is the summary representation of a course
     *
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary
     */
    suspend fun getCourseDetails(courseSummary: CourseSummary) : Course {
        return ionWebAPI.getFromURI<CourseProperties>(
            courseSummary.detailsUri).toCourse()
    }

}