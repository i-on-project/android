package org.ionproject.android.loading

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.ionwebapi.*
import org.ionproject.android.settings.Preferences
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URI

class RemoteConfigRepository(private val preferences: Preferences, mapper: JacksonIonMapper){

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: IonService = retrofit.create(IonService::class.java)

    private val ionWebAPI = IonWebAPI(service, mapper)

    suspend fun getRemoteConfig() =

        withContext(Dispatchers.IO){

            var remoteConfig: RemoteConfig?

            remoteConfig = ionWebAPI.getFromURI(URI("https://raw.githubusercontent.com/Jtoliveira/test/main/Remote_Config.json"),RemoteConfig::class.java, "application/json")

            val storedApiUrl = preferences.getWebApiHost()

            //update the stored API URL in case it changed
            if(remoteConfig.api_link != storedApiUrl){
                preferences.saveWebApiHost(remoteConfig.api_link)
            }
            Log.d("API", "remoteConfig in repo: $remoteConfig")

            remoteConfig
        }
}