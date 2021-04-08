package org.ionproject.android.common.ionwebapi

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

interface IonService {

    //@Headers("Authorization: Bearer $WEB_API_AUTHORIZATION_TOKEN")
    @GET
    suspend fun getFromUri(
        @Url uri: String,
        @Header("Accept") accept: String
    ): String

}
