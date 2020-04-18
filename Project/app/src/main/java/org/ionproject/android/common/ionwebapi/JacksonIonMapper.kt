package org.ionproject.android.common.ionwebapi

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
            jacksonObjMapper.readValue<SirenEntity>(responseBody)

        }
}