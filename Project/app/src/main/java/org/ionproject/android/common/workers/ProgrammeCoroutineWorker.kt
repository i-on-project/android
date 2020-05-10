package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
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

    private val programmeId by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getInt(PROGRAMME_ID_KEY, -1)
    }

    override suspend fun job() {
        if (programmeId != -1) {
            val programmeWithOffersLocal = programmesDao.getProgrammeWithOffersById(programmeId)
            if (programmeWithOffersLocal != null) {
                val programmeWithOffersServer =
                    ionWebAPI.getFromURI(
                        programmeWithOffersLocal.programme.selfUri,
                        SirenEntity::class.java
                    ).toProgramme()
                if (programmeWithOffersLocal != programmeWithOffersServer)
                    programmesDao.updateProgrammeWithOffers(programmeWithOffersServer)
            }
        }
    }

    override suspend fun lastJob() {
        if (programmeId != -1)
            programmesDao.deleteProgrammeWithOffersById(programmeId)
    }
}