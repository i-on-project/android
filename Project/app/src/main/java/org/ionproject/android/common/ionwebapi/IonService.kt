package org.ionproject.android.common.ionwebapi

import retrofit2.http.GET
import retrofit2.http.Url

interface IonService {
    @GET
    suspend fun getFromUri(@Url uri: String) : String
}