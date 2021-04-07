package org.ionproject.android.loading

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Url

interface RemoteConfigService {

    @GET("Jtoliveira/test/main/Remote_Config.json")
    suspend fun getRemoteConfig(): String
}