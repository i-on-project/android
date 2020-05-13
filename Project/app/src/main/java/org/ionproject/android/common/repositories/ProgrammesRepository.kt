package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.ProgrammeDao
import org.ionproject.android.common.db.ProgrammeOfferDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.programmes.toProgramme
import org.ionproject.android.programmes.toProgrammeOffer
import org.ionproject.android.programmes.toProgrammeSummaryList
import java.net.URI

private const val PROGRAMMES_ROOT_URI = "/v0/programmes"

class ProgrammesRepository(
    private val ionWebAPI: IIonWebAPI,
    private val programmeDao: ProgrammeDao,
    private val programmeOfferDao: ProgrammeOfferDao,
    private val workerManagerFacade: WorkerManagerFacade
) {

    /**
     * Performs a get request to the i-on API to obtain all the programmes,
     * and maps from [SirenEntity] to [List<ProgrammeSummary>].
     */
    suspend fun getAllProgrammes() =
        withContext(Dispatchers.IO) {
            var programmes = programmeDao.getAllProgrammeSummaries()

            if (programmes.count() == 0) {
                val uri = URI(PROGRAMMES_ROOT_URI)
                programmes =
                    ionWebAPI.getFromURI(uri, SirenEntity::class.java).toProgrammeSummaryList()
                val workerId = workerManagerFacade.enqueueWorkForAllProgrammeSummaries(
                    WorkImportance.NOT_IMPORTANT
                )
                programmes.forEach {
                    it.workerId = workerId
                }
                programmeDao.insertProgrammeSummaries(programmes)

            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(programmes.first())
            }
            programmes
        }

    /**
     * Performs a get request to the i-on API to obtain de details of a Programme,
     * and maps from [SirenEntity] to [ProgrammeSummary].
     */
    suspend fun getProgrammeDetails(
        programmeSummary: ProgrammeSummary
    ) = withContext(Dispatchers.IO) {
        var programmeWithOffers = programmeDao.getProgrammeWithOffersById(programmeSummary.id)

        if (programmeWithOffers == null) {
            programmeWithOffers = ionWebAPI.getFromURI<SirenEntity>(
                programmeSummary.detailsUri,
                SirenEntity::class.java
            ).toProgramme()
            val workerId = workerManagerFacade.enqueueWorkForProgramme(
                programmeWithOffers.programme,
                WorkImportance.NOT_IMPORTANT
            )
            programmeWithOffers.programme.workerId = workerId
            programmeDao.insertProgrammeWithOffers(programmeWithOffers)
        } else {
            workerManagerFacade.resetWorkerJobsByCacheable(programmeWithOffers.programme)
        }
        programmeWithOffers
    }


    /**
     * Performs a get request to the i-on API to obtain de details of a programmeOffer,
     * and maps from [SirenEntity] to [ProgrammeOffer].
     */
    suspend fun getProgrammeOfferDetails(programmeOfferSummary: ProgrammeOfferSummary) =
        withContext(Dispatchers.IO) {
            var programmeOffer = programmeOfferDao.getProgrammeOfferById(programmeOfferSummary.id)

            if (programmeOffer == null) {
                programmeOffer =
                    ionWebAPI.getFromURI(programmeOfferSummary.detailsUri, SirenEntity::class.java)
                        .toProgrammeOffer()
                val workerId = workerManagerFacade.enqueueWorkForProgrammeOffer(
                    programmeOffer,
                    WorkImportance.NOT_IMPORTANT
                )
                programmeOffer.workerId = workerId
                programmeOfferDao.insertProgrammeOffer(programmeOffer)
            } else {
                workerManagerFacade.resetWorkerJobsByCacheable(programmeOffer)
            }
            programmeOffer
        }
}