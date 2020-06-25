package org.ionproject.android.common.ionwebapi

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

private const val AUTHORIZATION_TOKEN = "5tuMrTm8dD-n2d53e3z2ALYC5JRUshAhZUa7xp3cHH4"

interface IonService {

    @Headers("Authorization: Bearer $AUTHORIZATION_TOKEN")
    @GET
    suspend fun getFromUri(@Url uri: String): String

}