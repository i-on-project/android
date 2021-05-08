package org.ionproject.android.offline

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.offline.models.*
import java.net.URI
import java.util.*

const val linkToCalendar =
    "https://raw.githubusercontent.com/i-on-project/integration-data/experimental/pt.ipl.isel/academic_years/2020-2021/calendar.json"

const val linkToCatalogProgrammesList =
    "https://api.github.com/repos/i-on-project/integration-data/git/trees/748698e9248d2bb20a2266cb78b386a37f39930d"

class CatalogRepository(private val webAPI: IIonWebAPI) {

    /**
     * Returns a list with all the programmes in the folder
     */
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

    /**
     * Returns a list with the available terms for a specific programme from the list
     */
    suspend fun getCatalogProgrammeTerms(linkToProgramme: URI) = withContext(Dispatchers.IO) {

        var catalogProgramme: CatalogProgrammeTerms?

        catalogProgramme = webAPI.getFromURI(
            linkToProgramme,
            CatalogProgrammeTerms::class.java,
            "application/json"
        )

        Log.d("Catalog", "catalog programme: $catalogProgramme")

        catalogProgramme
    }

    /**
     * Returns a list with the exam_schedule and timetable files for the specific term
     *
     * The filter in the end is because the integration repo has the files in json and yaml,
     * and this app deals exclusively in json
     */
    suspend fun getTermInfo(linkToInfo: URI) = withContext(Dispatchers.IO) {

        var catalogProgrammeTermInfo: CatalogProgrammeTermInfo?

        catalogProgrammeTermInfo = webAPI.getFromURI(
            linkToInfo,
            CatalogProgrammeTermInfo::class.java,
            "application/json"
        )

        Log.d("Catalog", "term info: $catalogProgrammeTermInfo")

        catalogProgrammeTermInfo.files.filter { it.fileName.contains("json") }
    }

    /**
     * Returns a parsed from base64 exam_schdule or timetable file, taking into account the version
     * of the device, since the base64 decoder is only available from version 26 onwards
     *
     * TODO: Maybe change the null return value for something better
     */
    suspend fun <T> getCatalogFile(fileLink: URI, branch: String?, programme: String?,term: String ?, klass: Class<T>): T?{

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val encodedFile: Base64EncodedFile =
                webAPI.getFromURI(
                    fileLink,
                    Base64EncodedFile::class.java,
                    "application/json"
                )

           return decodeFileContentFromGitHubApi(encodedFile.content, klass)

        }else{
            if (branch != null && programme != null && term != null) {
                return getFileFromGithub( branch, programme, term, klass)
            }
        }

        return null
    }

    /**
     * This function is used to decode Base64 encoded files from the "linkToInfo" value from the terms
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun <T> decodeFileContentFromGitHubApi(encodedString: String, klass: Class<T>): T {

        val clean = encodedString.replace("\n", "")

        val decodedBytes = Base64.getDecoder().decode(clean)
        val decoded = String(decodedBytes)

        return JacksonIonMapper().parse(decoded, klass)
    }

    suspend fun <T> getFileFromGithub(
        branch: String,
        programme: String,
        term: String,
        klass: Class<T>
    ): T {

        val link = when (klass) {
            ExamSchedule::class.java -> "https://raw.githubusercontent.com/i-on-project/integration-data/$branch/pt.ipl.isel/programmes/$programme/$term/exam_schedule.json"
            Timetable::class.java -> "https://raw.githubusercontent.com/i-on-project/integration-data/$branch/pt.ipl.isel/programmes/$programme/$term/timetable.json"
            else -> ""
        }

        return webAPI.getFromURI(
            URI(link),
            klass,
            "application/json"
        )
    }

}


