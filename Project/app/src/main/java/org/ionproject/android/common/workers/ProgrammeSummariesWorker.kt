package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.programmes.toProgrammeSummaryList

class ProgrammeSummariesWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val programmesDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.programmeDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    override suspend fun job(): Boolean {
        val programmeSummariesLocal = programmesDao.getAllProgrammeSummaries()
        val programmeSummariesServer =
            ionWebAPI.getFromURI(programmeSummariesLocal.first().selfUri, SirenEntity::class.java)
                .toProgrammeSummaryList()
        if (programmeSummariesServer.count() != programmeSummariesLocal.count()) {
            programmesDao.deleteAllProgrammeSummaries()
            programmesDao.insertProgrammeSummaries(programmeSummariesServer)
        } else {
            val summariesToUpdate = mutableListOf<ProgrammeSummary>()
            for (i in 0..programmeSummariesLocal.count() - 1) {
                if (programmeSummariesLocal[i] != programmeSummariesServer[i])
                    summariesToUpdate.add(programmeSummariesServer[i])
            }
            if (summariesToUpdate.count() > 0)
                programmesDao.updateProgrammeSummaries(summariesToUpdate)
        }
        return true
    }

    override suspend fun lastJob() {
        programmesDao.deleteAllProgrammeSummaries()
    }
}