package org.ionproject.android.common.ionwebapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

class IonWebAPI(private val ionService: IonService, private val ionMapper: IIonMapper) :
    IIonWebAPI {

    /**
     *  This is using the IO Dispacher, which is optimized to perform disk or network I/O
     *  outside of the main thread. Examples include using the Room component,
     *  reading from or writing to files, and running any network operations.
     */
    override suspend fun <T> getFromURI(uri: URI, klass: Class<T>): T {
        val response = withContext(Dispatchers.IO) {
            ionService.getFromUri(uri.toString())
        }
        return ionMapper.parse(response, klass)
    }
}