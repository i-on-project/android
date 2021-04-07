package org.ionproject.android.loading

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.IIonMapper
import org.ionproject.android.common.ionwebapi.IonService
import java.net.URI

class RemoteConfigAPI(private val remoteService: RemoteConfigService, private val ionMapper: IIonMapper) {

    suspend fun <T> getRemoteConfig(klass: Class<T>): T {

        val response = withContext(Dispatchers.IO) {
            remoteService.getRemoteConfig()
        }
        return ionMapper.parse(response, klass)
    }
}