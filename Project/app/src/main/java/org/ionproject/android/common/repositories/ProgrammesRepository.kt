package org.ionproject.android.common.repositories

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.ProgrammeDao
import org.ionproject.android.common.db.ProgrammeOfferDao
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.WEB_API_HOST
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.model.ProgrammeWithOffers
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import org.ionproject.android.programmes.toProgramme
import org.ionproject.android.programmes.toProgrammeOffer
import org.ionproject.android.programmes.toProgrammeSummaryList
import java.net.URI

class ProgrammesRepository(
    private val ionWebAPI: IIonWebAPI,
    private val programmeDao: ProgrammeDao,
    private val programmeOfferDao: ProgrammeOfferDao,
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * Performs a get request to the i-on API to obtain all the programmes,
     * and maps from [SirenEntity] to [List<ProgrammeSummary>].
     */
    suspend fun getAllProgrammes(programmesUri: URI) =
        withContext(dispatcher) {
            var programmes = programmeDao.getAllProgrammeSummaries()

            if (programmes.count() == 0) {
                programmes = ionWebAPI.getFromURI(programmesUri, SirenEntity::class.java)
                    .toProgrammeSummaryList()
                val workerId = workerManagerFacade.enqueueWorkForAllProgrammeSummaries(
                    WorkImportance.NOT_IMPORTANT,
                    programmesUri
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
        programmeDetailsUri: URI
    ): ProgrammeWithOffers? = withContext(dispatcher) {
        var programmeWithOffers = programmeDao.getProgrammeWithOffersByUri(URI("http://localhost:10023${programmeDetailsUri.path}"))

        if (programmeWithOffers == null) {
            programmeWithOffers = ionWebAPI.getFromURI(
                programmeDetailsUri,
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
        withContext(dispatcher) {
            var programmeOffer = programmeOfferDao.getProgrammeOfferById(programmeOfferSummary.id)

            if (programmeOffer == null) {
                programmeOffer =
                    ionWebAPI.getFromURI(URI("${WEB_API_HOST}${programmeOfferSummary.detailsUri.path}"), SirenEntity::class.java)
                        .toProgrammeOffer(programmeOfferSummary.courseId)
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
