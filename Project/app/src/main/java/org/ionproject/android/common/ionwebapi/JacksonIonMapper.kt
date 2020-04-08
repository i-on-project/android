package org.ionproject.android.common.ionwebapi

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.siren.SirenEntity

class JacksonIonMapper : IIonMapper {

    private val jacksonObjMapper = jacksonObjectMapper()

    /**
     * This is using the [Dispatchers.Default] because its optimized to perform CPU-intensive work
     * outside of the main thread.
     *
     * @param responseBody is the i-on Web API response body
     */
    override suspend fun parse(responseBody: String): SirenEntity =
        withContext(Dispatchers.Default) {
            Log.println(Log.DEBUG, "COURSES", "Started parsing to SirenEntity")
            val res = kotlin.runCatching {
                jacksonObjMapper.readValue<SirenEntity>(responseBody)
            }
            if (res.isFailure) {
                Log.println(
                    Log.DEBUG,
                    "COURSES",
                    "Failed parsing to SirenEntity, exception : ${res.exceptionOrNull()}"
                )
                throw res.exceptionOrNull()!! //This will never return null here
            }
            Log.println(Log.DEBUG, "COURSES", "Successfully parsed to SirenEntity")
            res.getOrNull()!!
        }
}