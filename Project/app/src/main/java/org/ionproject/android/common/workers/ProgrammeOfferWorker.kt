package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.programmes.toProgrammeOffer

class ProgrammeOfferWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val programmeOfferDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.programmeOfferDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val programmeOfferId by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getInt(PROGRAMME_OFFER_ID_KEY, -1)
    }

    override suspend fun job(): Boolean {
        if (programmeOfferId != -1) {
            val programmeOfferLocal = programmeOfferDao.getProgrammeOfferById(programmeOfferId)
            if (programmeOfferLocal != null) {
                val programmeOfferServer =
                    ionWebAPI.getFromURI(programmeOfferLocal.selfUri, SirenEntity::class.java)
                        .toProgrammeOffer()
                if (programmeOfferLocal != programmeOfferServer) {
                    programmeOfferDao.updateProgrammeOffer(programmeOfferServer)
                }
            }
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (programmeOfferId != -1)
            programmeOfferDao.deleteProgrammeOfferById(programmeOfferId)
    }
}