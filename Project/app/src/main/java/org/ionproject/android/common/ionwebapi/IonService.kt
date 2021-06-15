package org.ionproject.android.common.ionwebapi

import okhttp3.RequestBody
import org.ionproject.android.userAPI.models.SelectedMethod
import retrofit2.Response
import retrofit2.http.*

interface IonService {

    @GET
    suspend fun getFromUri(
        @Url uri: String,
        @Header("Accept") accept: String,
        @Header("Authorization") bearerToken: String
    ): String

    @PUT
    suspend fun addClassSectionToCoreFavourites(
        @Url uri: String,
        @Header("Authorization") bearerToken: String
    ): String

    /**
     *
     * This method requires Response<Unit> because the backend service returns
     * non null body, despite having no content
     */
    @DELETE
    suspend fun removeClassSectionFromCoreFavourites(
        @Url uri: String,
        @Header("Authorization") bearerToken: String
    ): Response<Unit>

    @GET
    suspend fun getFromUriWithoutAuth(
        @Url uri: String,
        @Header("Accept") accept: String
    ): String

    @POST
    suspend fun pollCore(
        @Url uri: String,
        @Header("Accept") accept: String
    ): String

    @Headers( "Content-Type: application/json" )
    @POST
    suspend fun loginWithEmail(
        @Url uri: String,
        @Header("Accept") accept: String,
        @Body body: RequestBody
    ): String


}
