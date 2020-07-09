package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toClassCollection
import java.net.URI

class ClassCollectionWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val classCollectionDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.classCollectionDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val courseAcronym by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(CLASS_SUMMARIES_COURSE_KEY) ?: ""
    }

    private val calendarTerm by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(CLASS_SUMMARIES_CALENDAR_TERM_KEY) ?: ""
    }

    private val classCollectionUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (classCollectionUri != "") {
            val classCollectionServer =
                ionWebAPI.getFromURI(URI(classCollectionUri), SirenEntity::class.java)
                    .toClassCollection()
            classCollectionDao.updateClassCollection(classCollectionServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (classCollectionUri != "" && courseAcronym != "" && calendarTerm != "") {
            classCollectionDao.deleteClassCollectionFieldsByUri(URI(classCollectionUri))
            classCollectionDao.deleteClassSummariesByCourseAndCalendarTerm(
                courseAcronym,
                calendarTerm
            )
        }
    }

}