package org.ionproject.android.common.repositories

import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.programmes.toProgramme
import org.ionproject.android.programmes.toProgrammeOffer
import org.ionproject.android.programmes.toProgrammeSummaryList
import java.net.URI

class ProgrammesRepository(private val ionWebAPI: IIonWebAPI) {

    /**
     * Performs a get request to the i-on API to obtain all the programmes,
     * and maps from [SirenEntity] to [List<ProgrammeSummary>].
     */
    suspend fun getAllProgrammes(): List<ProgrammeSummary> {
        val uri = URI("/v0/programmes")
        return ionWebAPI.getFromURI(uri).toProgrammeSummaryList()
    }

    /**
     * Performs a get request to the i-on API to obtain de details of a Programme,
     * and maps from [SirenEntity] to [ProgrammeSummary].
     */
    suspend fun getProgrammeDetails(programmeSummary: ProgrammeSummary) =
        ionWebAPI.getFromURI(programmeSummary.detailsUri).toProgramme()

    /**
     * Performs a get request to the i-on API to obtain de details of a programmeOffer,
     * and maps from [SirenEntity] to [ProgrammeOffer].
     */
    suspend fun getProgrammeOfferDetails(programmeOfferSummary: ProgrammeOfferSummary) =
        ionWebAPI.getFromURI(programmeOfferSummary.detailsUri).toProgrammeOffer()

}