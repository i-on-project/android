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
    "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/academic_years/2020-2021/calendar.json"

const val linkToCatalogProgrammesList =
    "https://api.github.com/repos/i-on-project/integration-data/git/trees/748698e9248d2bb20a2266cb78b386a37f39930d"

const val linkToExamSchedule = "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/programmes/%s/%s/exam_schedule.json"
const val linkToTimeTable = "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/programmes/%s/%s/timetable.json"

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
     * NOTE: Since the parsing of the base64 file was causing problems, the non 4head solution is
     * just using the code that works with every version instead of this version checking nonsense
     */
    suspend fun <T> getFileFromGithub(
        programme: String,
        term: String,
        klass: Class<T>
    ): T {

        val link = when (klass) {
            ExamSchedule::class.java -> linkToExamSchedule.format(programme, term)
            Timetable::class.java -> linkToTimeTable.format(programme, term)
            else -> ""
        }

        return webAPI.getFromURI(
            URI(link),
            klass,
            "application/json"
        )
    }

        /*return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val encodedFile: Base64EncodedFile =
                webAPI.getFromURI(
                    fileLink,
                    Base64EncodedFile::class.java,
                    "application/json"
                )

            decodeFileContentFromGitHubApi(encodedFile.content, klass)

        }else{

        }
    }

    /**
     * This function is used to decode Base64 encoded files from the "linkToInfo" value from the terms
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun <T> decodeFileContentFromGitHubApi(encodedString: String, klass: Class<T>): T {

        val clean = encodedString.replace("\n", "")

        val decodedBytes = Base64.getDecoder().decode(clean)
        val decoded = String(decodedBytes)

        print(decoded)

        return JacksonIonMapper().parse(decoded, klass)
    }*/
}


