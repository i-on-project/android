package org.ionproject.android.common.ionwebapi

import retrofit2.http.*

interface IonService {

    @GET
    suspend fun getFromUri(
        @Url uri: String,
        @Header("Accept") accept: String
    ): String
}
