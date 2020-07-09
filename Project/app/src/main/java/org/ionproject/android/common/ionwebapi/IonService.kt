package org.ionproject.android.common.ionwebapi

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

private const val AUTHORIZATION_TOKEN = "5HyGXjRIZiUj6s09FoRT7FJqbP8dGhTvqO_7gwHCk8E\n"

interface IonService {

    @Headers("Authorization: Bearer $AUTHORIZATION_TOKEN")
    @GET
    suspend fun getFromUri(@Url uri: String): String

}
