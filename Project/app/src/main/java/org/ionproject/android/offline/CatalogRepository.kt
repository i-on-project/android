package org.ionproject.android.offline

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.offline.models.*
import java.net.URI

const val linkToCalendar =
    "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/academic_years/%s/calendar.json" //%s is the selected year

const val linkToCatalogProgrammesList =
    "https://api.github.com/repos/i-on-project/integration-data/git/trees/748698e9248d2bb20a2266cb78b386a37f39930d"

const val linkToAcademicYears =
    "https://api.github.com/repos/i-on-project/integration-data/git/trees/17c361250d14345325587c21c7840b97af944f79"

const val linkToExamSchedule =
    "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/programmes/%s/%s/exam_schedule.json"
const val linkToTimeTable =
    "https://raw.githubusercontent.com/i-on-project/integration-data/master/pt.ipl.isel/programmes/%s/%s/timetable.json"

class CatalogRepository(private val webAPI: IIonWebAPI) {

    /**
     * Returns a list with all the programmes in the folder
     */
    suspend fun getCatalogProgrammeList() = withContext(Dispatchers.IO) {

        var catalogProgrammeList: CatalogProgrammes?

        catalogProgrammeList = webAPI.getFromURIWithoutAuth(
            URI(linkToCatalogProgrammesList),
            CatalogProgrammes::class.java,
            "application/json"
        )

        catalogProgrammeList
    }

    /**
     * Returns a list with the available terms for a specific programme from the list
     */
    suspend fun getCatalogProgrammeTerms(linkToProgramme: URI) = withContext(Dispatchers.IO) {

        var catalogProgramme: CatalogProgrammeTerms?

        catalogProgramme = webAPI.getFromURIWithoutAuth(
            linkToProgramme,
            CatalogProgrammeTerms::class.java,
            "application/json"
        )

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

        catalogProgrammeTermInfo = webAPI.getFromURIWithoutAuth(
            linkToInfo,
            CatalogProgrammeTermInfo::class.java,
            "application/json"
        )

        catalogProgrammeTermInfo.files.filter { it.fileName.contains("json") }
    }


    /**
     * NOTE: Since the parsing of the base64 file was causing problems, we use this
     * simpler solution that works for all Android versions
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

        return webAPI.getFromURIWithoutAuth(
            URI(link),
            klass,
            "application/json"
        )
    }

    suspend fun getCatalogAcademicYears() = withContext(Dispatchers.IO) {

        var catalogAcademicYears: CatalogAcademicYears?

        catalogAcademicYears = webAPI.getFromURIWithoutAuth(
            URI(linkToAcademicYears),
            CatalogAcademicYears::class.java,
            "application/json"
        )

        catalogAcademicYears
    }

    suspend fun getCatalogCalendar(year: String) = withContext(Dispatchers.IO) {

        var calendar: CatalogCalendar?

        calendar = webAPI.getFromURIWithoutAuth(
            URI(linkToCalendar.format(year)),
            CatalogCalendar::class.java,
            "application/json"
        )

        calendar
    }
}


