package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.course_details.toClassSummaryList

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

    override suspend fun job(): Boolean {
        if (courseAcronym != "" && calendarTerm != "") {
            val classSummariesLocal =
                classSummaryDao.getClassSummariesByCourseAndCalendarTerm(
                    courseAcronym,
                    calendarTerm
                )
            val classSummariesServer =
                ionWebAPI.getFromURI(classSummariesLocal[0].selfUri, SirenEntity::class.java)
                    .toClassSummaryList()
            if (classSummariesServer.count() != classSummariesLocal.count()) {
                classSummaryDao.deleteClassSummaries(classSummariesLocal)
                classSummaryDao.insertClassSummaries(classSummariesServer)
            } else {
                val summariesToUpdate = mutableListOf<ClassSummary>()

                for (i in 0..classSummariesLocal.count() - 1) {
                    if (classSummariesLocal[i] != classSummariesServer[i])
                        summariesToUpdate.add(classSummariesServer[i])
                }
                if (summariesToUpdate.count() > 0)
                    classSummaryDao.updateClassSummaries(summariesToUpdate)
            }
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