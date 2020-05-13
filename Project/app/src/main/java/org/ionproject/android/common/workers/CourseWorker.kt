package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toCourse

class CourseWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val courseDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.courseDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val courseId by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getInt(COURSE_DETAILS_ID_KEY, -1)
    }

    override suspend fun job(): Boolean {
        if (courseId != -1) {
            val courseLocal = courseDao.getCourseById(courseId)
            if (courseLocal != null) {
                val courseServer =
                    ionWebAPI.getFromURI(courseLocal.selfUri, SirenEntity::class.java).toCourse()
                if (courseLocal != courseServer)
                    courseDao.updateCourse(courseServer)
            }
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (courseId != -1)
            courseDao.deleteCourseById(courseId)
    }
}