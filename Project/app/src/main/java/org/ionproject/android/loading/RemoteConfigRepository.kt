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

class RemoteConfigRepository(private val sharedPreferences: SharedPreferences, mapper: JacksonIonMapper){

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: IonService = retrofit.create(IonService::class.java)

    private val ionWebAPI = IonWebAPI(service, mapper)

    private fun saveAPIURLToSharedPreferences(newURL:String) =
        sharedPreferences.edit().putString("WEB_API_HOST", newURL).apply()

    suspend fun getRemoteConfig() =

        withContext(Dispatchers.IO){

            var remoteConfig: RemoteConfig?

            remoteConfig = ionWebAPI.getFromURI(URI("https://raw.githubusercontent.com/Jtoliveira/test/main/Remote_Config.json"),RemoteConfig::class.java, "application/json")

            val storedApiUrl = sharedPreferences.getString("WEB_API_HOST", WEB_API_HOST)

            //update the stored API URL in case it changed
            if(remoteConfig.api_link != storedApiUrl){
                saveAPIURLToSharedPreferences(remoteConfig.api_link)
            }
            Log.d("API", "remoteConfig in repo: $remoteConfig")

            remoteConfig
        }
}