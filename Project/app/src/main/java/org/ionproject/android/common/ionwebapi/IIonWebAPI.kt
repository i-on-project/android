package org.ionproject.android.common.ionwebapi

import okhttp3.RequestBody
import org.ionproject.android.common.dto.APPLICATION_TYPE
import org.ionproject.android.common.dto.SIREN_SUBTYPE
import org.ionproject.android.userAPI.models.AuthMethod
import org.ionproject.android.userAPI.models.SelectedMethod
import org.ionproject.android.userAPI.models.SelectedMethodResponse
import java.net.URI

const val SIREN_MEDIA_TYPE = "$APPLICATION_TYPE/$SIREN_SUBTYPE"
const val JSON_HOME_MEDIA_TYPE = "$APPLICATION_TYPE/json-home"

interface IIonWebAPI {

    /**
     * This method should perform an http request to the i-on Web API and parse the response to
     * Siren Entity
     */
    suspend fun <T> getFromURI(uri: URI, klass: Class<T>, accept: String = SIREN_MEDIA_TYPE, bearerToken: String = "Bearer $WEB_API_AUTHORIZATION_TOKEN"): T

    suspend fun addClassSectionToCoreFavourites(uri: URI, bearerToken: String = "Bearer $USER_API_ACCESS_TOKEN"): String

    suspend fun removeClassSectionFromCoreFavourites(uri: URI, bearerToken: String = "Bearer $USER_API_ACCESS_TOKEN")

    suspend fun <T> getFromURIWithoutAuth(uri: URI, klass: Class<T>, accept: String = SIREN_MEDIA_TYPE): T

    suspend fun <T> pollCore(uri: URI,  klass: Class<T>, accept: String = SIREN_MEDIA_TYPE): T

    suspend fun getAuthMethodsArray(uri: URI, accept: String = SIREN_MEDIA_TYPE): List<AuthMethod>

    suspend fun <T> loginWithEmail(uri: URI, accept: String = SIREN_MEDIA_TYPE, klass: Class<T>, body: RequestBody): T
}