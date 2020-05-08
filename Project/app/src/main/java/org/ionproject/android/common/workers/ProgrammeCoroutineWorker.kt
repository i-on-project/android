package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.programmes.toProgramme

class ProgrammeCoroutineWorker(
    context: Context,
    params: WorkerParameters
) : NumberedCoroutineWorker(context, params) {

    private val programmesDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.programmeDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    override suspend fun job(resourceId: Int) {
        val programmeWithOffersLocal = programmesDao.getProgrammeWithOffersById(resourceId)
        if (programmeWithOffersLocal != null) {
            val programmeWithOffersServer =
                ionWebAPI.getFromURI(programmeWithOffersLocal.programme.selfUri).toProgramme()
            if (programmeWithOffersLocal != programmeWithOffersServer)
                programmesDao.updateProgrammeWithOffers(programmeWithOffersServer)
        }
    }

    override suspend fun lastJob(resourceId: Int) {
        programmesDao.deleteProgrammeWithOffersById(resourceId)
    }
}