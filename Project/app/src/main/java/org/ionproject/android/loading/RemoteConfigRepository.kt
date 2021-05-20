package org.ionproject.android.loading

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.REMOTE_CONFIG_LINK
import org.ionproject.android.settings.Preferences
import java.net.URI

class RemoteConfigRepository(val preferences: Preferences, private val webAPI: IIonWebAPI) {

    suspend fun getRemoteConfig() =

        withContext(Dispatchers.IO) {

            var remoteConfig: RemoteConfig?

            remoteConfig = webAPI.getFromURI(
                URI(REMOTE_CONFIG_LINK),
                RemoteConfig::class.java,
                "application/json"
            )

            val storedApiUrl = preferences.getWebApiHost()

            //update the stored API URL in case it changed
            if (remoteConfig.api_link != storedApiUrl) {
                preferences.saveWebApiHost(remoteConfig.api_link)
            }
            Log.d("API", "remoteConfig in repo: $remoteConfig")

            remoteConfig
        }
}