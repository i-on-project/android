package org.ionproject.android.offline

import android.os.Build
import androidx.annotation.RequiresApi
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import java.net.URI
import java.util.*

const val linkToExamSchedule =
    "https://raw.githubusercontent.com/i-on-project/integration-data/experimental/pt.ipl.isel/programmes/leic/2020-2021-2/exam_schedule.json"

const val linkToTimetable =
    "https://raw.githubusercontent.com/i-on-project/integration-data/experimental/pt.ipl.isel/programmes/leic/2020-2021-2/timetable.json"

class Utils {

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
        webAPI: IIonWebAPI,
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