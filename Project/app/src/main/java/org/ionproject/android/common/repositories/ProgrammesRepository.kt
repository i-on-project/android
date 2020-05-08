package org.ionproject.android.common.repositories

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.db.ProgrammeDao
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.model.ProgrammeWithOffers
import org.ionproject.android.common.workers.ProgrammeCoroutineWorker
import org.ionproject.android.common.workers.ProgrammeSummariesCoroutineWorker
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
    private val workerManagerFacade: WorkerManagerFacade
) {

    /**
     * Performs a get request to the i-on API to obtain all the programmes,
     * and maps from [SirenEntity] to [List<ProgrammeSummary>].
     */
    suspend fun getAllProgrammes(onResult: (List<ProgrammeSummary>) -> Unit) = coroutineScope {
        var programmes = programmeDao.getAllProgrammeSummaries()

        if (programmes.count() == 0) {
            val uri = URI(PROGRAMMES_ROOT_URI)
            programmes = ionWebAPI.getFromURI(uri).toProgrammeSummaryList()
            launch {
                programmeDao.insertProgrammeSummaries(programmes)
                workerManagerFacade.enqueuePeriodicWork(
                    WorkImportance.IMPORTANT,
                    ProgrammeSummariesCoroutineWorker::class.java,
                    programmes.first()
                )
            }
        } else {
            launch {
                workerManagerFacade.resetWorkerJobsByResource(programmes.first())
            }
        }

        onResult(programmes)
    }

    /**
     * Performs a get request to the i-on API to obtain de details of a Programme,
     * and maps from [SirenEntity] to [ProgrammeSummary].
     */
    suspend fun getProgrammeDetails(
        programmeSummary: ProgrammeSummary,
        onResult: suspend (ProgrammeWithOffers) -> Unit
    ) = coroutineScope {
        var programmeWithOffers = programmeDao.getProgrammeWithOffersById(programmeSummary.id)

        if (programmeWithOffers == null) {
            programmeWithOffers = ionWebAPI.getFromURI(programmeSummary.detailsUri).toProgramme()
            launch {
                programmeDao.insertProgrammeWithOffers(programmeWithOffers)
                workerManagerFacade.enqueuePeriodicWork(
                    WorkImportance.IMPORTANT,
                    ProgrammeCoroutineWorker::class.java,
                    programmeWithOffers.programme
                )
            }
        } else {
            launch {
                workerManagerFacade.resetWorkerJobsByResource(programmeWithOffers.programme)
            }
        }
        onResult(programmeWithOffers)
    }


    /**
     * Performs a get request to the i-on API to obtain de details of a programmeOffer,
     * and maps from [SirenEntity] to [ProgrammeOffer].
     */
    suspend fun getProgrammeOfferDetails(programmeOfferSummary: ProgrammeOfferSummary) =
        ionWebAPI.getFromURI(programmeOfferSummary.detailsUri).toProgrammeOffer(
            programmeOfferSummary.programmeId
        )

}