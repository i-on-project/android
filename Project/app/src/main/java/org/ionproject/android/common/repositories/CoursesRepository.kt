package org.ionproject.android.common.repositories

import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.model.CourseSummary
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toCourse
import org.ionproject.android.courses.toCourseSummaryList
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
     instead of using a hardcoded URI. This should also go to the DB instead of
     the WebAPI all the time, so that the app works in offline mode.
     */
    suspend fun getAllCourses(): List<CourseSummary> {
        //TODO This variable is temporary, its here until the read API has curricular terms
        val uri = URI("/v0/courses")
        return ionWebAPI
            .getFromURI(uri, SirenEntity::class.java)
            .toCourseSummaryList()
    }

    /**
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary.
     *
     * @param courseSummary is the summary representation of a course
     */
    suspend fun getCourseDetails(courseSummary: CourseSummary): Course {
        return ionWebAPI.getFromURI(
            courseSummary.detailsUri,
            SirenEntity::class.java
        ).toCourse()
    }

}
