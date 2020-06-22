package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toCourse
import java.net.URI

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

    private val courseUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (courseId != -1 && courseUri != "") {
            val courseServer =
                ionWebAPI.getFromURI(URI(courseUri), SirenEntity::class.java).toCourse()
            courseDao.updateCourse(courseServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (courseId != -1)
            courseDao.deleteCourseById(courseId)
    }
}