package org.ionproject.android.common.repositories

import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.course_details.toCourse

/**
 * This type represents a course repository, it should request
 * from the API, and map the result to the app model.
 */
class CourseRepository(private val ionWebAPI: IIonWebAPI) {

    /**
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary.
     *
     * @param courseSummary is the summary representation of a course
     */
    suspend fun getCourseDetails(programmeOffer: ProgrammeOffer): Course {
        return ionWebAPI.getFromURI(
            programmeOffer.detailsUri
        ).toCourse()
    }

}
