package org.ionproject.android.common.ionwebapi

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JacksonIonMapper : IIonMapper {

    private val mapper = ObjectMapper().registerKotlinModule().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    }

    /**
     * This is using the [Dispatchers.Default] because its optimized to perform CPU-intensive work
     * outside of the main thread.
     *
     * @param responseBody is the i-on Web API response body
     */
    override suspend fun <T> parse(responseBody: String, klass: Class<T>): T =
        withContext(Dispatchers.Default) {
            val reader = mapper.readerFor(klass)
            reader.readValue<T>(responseBody)
        }

}

