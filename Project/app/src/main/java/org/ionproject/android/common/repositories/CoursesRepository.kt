package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.CourseDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.course_details.toCourse

/**
 * This type represents a course repository, it should request
 * from the API, and map the result to the app model.
 */
class CourseRepository(
    private val ionWebAPI: IIonWebAPI,
    private val courseDao: CourseDao,
    private val workerManagerFacade: WorkerManagerFacade
) {

    /**
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary.
     *
     * @param courseSummary is the summary representation of a course
     */
    suspend fun getCourseDetails(programmeOffer: ProgrammeOffer) =
        withContext(Dispatchers.IO) {
            var course = courseDao.getCourseById(programmeOffer.courseID)

            if (course == null) {
                course = ionWebAPI.getFromURI(programmeOffer.detailsUri, SirenEntity::class.java)
                    .toCourse(programmeOffer.termNumber)
                val workerId = workerManagerFacade.enqueueWorkForCourse(
                    course,
                    WorkImportance.IMPORTANT
                )
                course.workerId = workerId
                courseDao.insertCourse(course)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(course)
            }
            course
        }
}
