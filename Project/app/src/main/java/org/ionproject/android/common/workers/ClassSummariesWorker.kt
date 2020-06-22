package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.course_details.toClassSummaryList
import java.net.URI

class ClassSummariesWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val classSummaryDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.classSummaryDao()
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

    private val classSummariesUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (courseAcronym != "" && calendarTerm != "" && classSummariesUri != "") {
            val classSummariesServer =
                ionWebAPI.getFromURI(URI(classSummariesUri), SirenEntity::class.java)
                    .toClassSummaryList()
            classSummaryDao.deleteClassSummariesByCourseAndCalendarTerm(courseAcronym, calendarTerm)
            classSummaryDao.insertClassSummaries(classSummariesServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (courseAcronym != "" && calendarTerm != "") {
            classSummaryDao.deleteClassSummariesByCourseAndCalendarTerm(courseAcronym, calendarTerm)
        }
    }

}