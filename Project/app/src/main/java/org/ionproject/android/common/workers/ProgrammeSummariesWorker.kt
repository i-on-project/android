package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.programmes.toProgrammeSummaryList
import java.net.URI

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

    private val programmeSummariesUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if(programmeSummariesUri != "") {
            val programmeSummariesServer =
                ionWebAPI.getFromURI(URI(programmeSummariesUri), SirenEntity::class.java)
                    .toProgrammeSummaryList()
            programmesDao.deleteAllProgrammeSummaries()
            programmesDao.insertProgrammeSummaries(programmeSummariesServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        programmesDao.deleteAllProgrammeSummaries()
    }
}