package org.ionproject.android.common.workers

import android.content.Context
import androidx.work.WorkerParameters
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.programmes.toProgramme
import java.net.URI

class ProgrammeWorker(
    context: Context,
    params: WorkerParameters
) : NumberedWorker(context, params) {

    private val programmesDao by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.db.programmeDao()
    }

    private val ionWebAPI by lazy(LazyThreadSafetyMode.NONE) {
        IonApplication.ionWebAPI
    }

    private val programmeId by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getInt(PROGRAMME_ID_KEY, -1)
    }

    private val programmeUri by lazy(LazyThreadSafetyMode.NONE) {
        inputData.getString(RESOURCE_URI_KEY) ?: ""
    }

    override suspend fun job(): Boolean {
        if (programmeId != -1 && programmeUri != "") {
            val programmeWithOffersServer =
                ionWebAPI.getFromURI(
                    URI(programmeUri),
                    SirenEntity::class.java
                ).toProgramme()
            programmesDao.updateProgrammeWithOffers(programmeWithOffersServer)
            return true
        }
        return false
    }

    override suspend fun lastJob() {
        if (programmeId != -1)
            programmesDao.deleteProgrammeWithOffersById(programmeId)
    }
}