package org.ionproject.android.common.repositories

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.CourseDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.course_details.toCourse
import java.net.URI

/**
 * This type represents a course repository, it should request
 * from the API, and map the result to the app model.
 */
class CourseRepository(
    private val ionWebAPI: IIonWebAPI,
    private val courseDao: CourseDao,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * For now this is the received parameter, because for a user to get to
     * the details of a course it first has to get its summary.
     *
     * @param courseSummary is the summary representation of a course
     */
    suspend fun getCourseDetails(courseDetailsUri: URI): Course? =
        withContext(dispatcher) {
            var course = courseDao.getCourseByUri(courseDetailsUri)

            if (course == null) {
                course = ionWebAPI.getFromURI(courseDetailsUri, SirenEntity::class.java)
                    .toCourse()
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
