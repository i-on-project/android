package org.ionproject.android.offline

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import java.net.URI

const val linkToCalendar =
    "https://raw.githubusercontent.com/i-on-project/integration-data/experimental/pt.ipl.isel/2020-2021/calendar.json"

const val linkToCatalogProgrammesList =
    "https://api.github.com/repos/i-on-project/integration-data/git/trees/748698e9248d2bb20a2266cb78b386a37f39930d"

class CatalogRepository(private val webAPI: IIonWebAPI) {

    suspend fun getCatalogProgrammeList() = withContext(Dispatchers.IO) {

        var catalogProgrammeList: CatalogProgrammes?

        catalogProgrammeList = webAPI.getFromURI(
            URI(linkToCatalogProgrammesList),
            CatalogProgrammes::class.java,
            "application/json"
        )

        Log.d("Catalog", "catalog programme list: $catalogProgrammeList")

        catalogProgrammeList
    }

    suspend fun getCatalogProgramme(linkToProgramme: String) = withContext(Dispatchers.IO) {

        var catalogProgramme: CatalogProgramme?

        catalogProgramme = webAPI.getFromURI(
            URI(linkToProgramme),
            CatalogProgramme::class.java,
            "application/json"
        )

        Log.d("Catalog", "catalog programme: $catalogProgramme")

        catalogProgramme
    }

    suspend fun getTermInfo(linkToInfo: String) = withContext(Dispatchers.IO) {

        var catalogProgrammeTermInfo: CatalogProgrammeTermInfo?

        catalogProgrammeTermInfo = webAPI.getFromURI(
            URI(linkToInfo),
            CatalogProgrammeTermInfo::class.java,
            "application/json"
        )

        Log.d("Catalog", "term info: $catalogProgrammeTermInfo")

        catalogProgrammeTermInfo
    }

}


