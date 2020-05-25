package org.ionproject.android.common.ionwebapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI

class IonWebAPI(private val ionService: IonService, private val ionMapper: IIonMapper) : IIonWebAPI {

    override suspend fun <T> getFromURI(uri: URI, klass: Class<T>): T {
        val response = withContext(Dispatchers.IO) {
            ionService.getFromUri(uri.toString())
        }
        return ionMapper.parse(response,klass)
    }
}