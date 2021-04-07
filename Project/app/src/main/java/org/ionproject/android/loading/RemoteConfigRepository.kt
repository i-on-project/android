package org.ionproject.android.loading

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.IonApplication.Companion.ionMapper
import org.ionproject.android.common.ionwebapi.*
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URI

private val remoteConfigURL = URI("https://raw.githubusercontent.com/Jtoliveira/test/main/Remote_Config.json")

class RemoteConfigRepository(private val ionWebAPI: IIonWebAPI, private val sharedPreferences: SharedPreferences){

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: RemoteConfigService = retrofit.create(RemoteConfigService::class.java)

    private val remoteConfigAPI = RemoteConfigAPI(service, JacksonIonMapper())

    private fun saveAPIURLToSharedPreferences(newURL:String) =
        sharedPreferences.edit().putString("WEB_API_HOST", newURL).apply()

    suspend fun getRemoteConfig() =

        withContext(Dispatchers.IO){

            var remoteConfig: RemoteConfig?

            remoteConfig = remoteConfigAPI.getRemoteConfig(RemoteConfig::class.java)

            val storedApiUrl = sharedPreferences.getString("WEB_API_HOST", WEB_API_HOST)

            //update the stored API URL in case it changed
            if(remoteConfig.api_link != storedApiUrl){
                saveAPIURLToSharedPreferences(remoteConfig.api_link)
            }
            Log.i("pls", "remoteConfig in repo: $remoteConfig")

            remoteConfig
        }
}