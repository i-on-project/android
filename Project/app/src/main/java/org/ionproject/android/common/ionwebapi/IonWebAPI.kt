package org.ionproject.android.common.ionwebapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.dto.APPLICATION_TYPE
import org.ionproject.android.common.dto.SIREN_SUBTYPE
import java.net.URI

class IonWebAPI(private val ionService: IonService, private val ionMapper: IIonMapper) :
    IIonWebAPI {

    /**
     *  This is using the IO Dispacher, which is optimized to perform disk or network I/O
     *  outside of the main thread. Examples include using the Room component,
     *  reading from or writing to files, and running any network operations.
     */
    override suspend fun <T> getFromURI(
        uri: URI,
        klass: Class<T>,
        accept: String
    ): T {
        val response = withContext(Dispatchers.IO) {
            ionService.getFromUri(uri.toString(), accept)
        }
        return ionMapper.parse(response, klass)
    }
}