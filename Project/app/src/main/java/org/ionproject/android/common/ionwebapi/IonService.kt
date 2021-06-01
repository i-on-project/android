package org.ionproject.android.common.ionwebapi

import okhttp3.RequestBody
import org.ionproject.android.userAPI.models.SelectedMethod
import retrofit2.http.*

interface IonService {

    @Headers("Authorization: Bearer $WEB_API_AUTHORIZATION_TOKEN")
    @GET
    suspend fun getFromUri(
        @Url uri: String,
        @Header("Accept") accept: String
    ): String

    @GET
    suspend fun getFromUriWithoutAuth(
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
