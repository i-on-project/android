package org.ionproject.android.common.repositories

import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.model.CourseSummary
import org.ionproject.android.common.siren.EmbeddedEntity
import org.ionproject.android.course_details.CourseProperties
import org.ionproject.android.course_details.toCourse
import java.net.URI

/**
 * This type represents a course repository, it should request
 * from the API, and map the result to the app model.
 */
class CourseRepository(private val ionWebAPI: IIonWebAPI) {

    /**
     * Performs a get request to the i-on API to obtain all the courses,
     * and maps from [SirenEntity] to [Course].
     */

    /*
    TODO: Should receive the curricular term and get the courses from that term
     instead of using an hardcoded URI. This should also go to the DB instead of
     the WebAPI all the time, so that the app works in offline mode.
     */

    suspend fun getAllCourses(): List<CourseSummary> {
        val uri = URI("/v0/courses")
        val coursesList = mutableListOf<CourseSummary>()

        ionWebAPI.getFromURI<Any>(uri).entities?.forEach {
            val embeddedEntity = (it as EmbeddedEntity<LinkedHashMap<String, String>>)

            val acr = embeddedEntity.properties?.get("acronym")
            val detailsUri = embeddedEntity.links?.first()?.href

            if (acr != null && detailsUri != null)
                coursesList.add(CourseSummary(acronym = acr, detailsUri = detailsUri))
        }

        return coursesList
    }

    /**
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary.
     *
     * @param courseSummary is the summary representation of a course
     */
    suspend fun getCourseDetails(courseSummary: CourseSummary): Course {
        return ionWebAPI.getFromURI<CourseProperties>(
            courseSummary.detailsUri
        ).toCourse()
    }

}
