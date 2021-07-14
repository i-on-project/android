package org.ionproject.android.userAPI

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.ionproject.android.common.ionwebapi.*
import org.ionproject.android.userAPI.models.*
import java.net.URI

class UserAPIRepository(private val webAPI: IIonWebAPI) {

    suspend fun getAuthMethods() =
        withContext(Dispatchers.IO) {

            val availableMethods = webAPI.getAuthMethodsArray(
                URI(AUTH_METHODS_LINK),
                "application/json"
            )

            availableMethods
        }

    suspend fun loginWithEmail(body: SelectedMethod) =

        withContext(Dispatchers.IO) {

            val mapper = ObjectMapper() //jackson mapper so we can send a request body in JSON

            val json = mapper.writeValueAsString(body)

            val loginResponse = webAPI.postWithBody(
                URI(AUTHENTICATION_ENDPOINT),
                "application/json",
                SelectedMethodResponse::class.java,
                RequestBody.create(MediaType.parse("application/json"), json)
            )
            loginResponse
        }

    suspend fun pollCoreForAuthentication(body: PollBody) =
        withContext(Dispatchers.IO) {

            val mapper = ObjectMapper() //jackson mapper so we can send a request body in JSON

            val json = mapper.writeValueAsString(body)

            var pollResponse = webAPI.postWithBody(
                URI(CORE_POLL_LINK),
                "application/json",
                PollResponse::class.java,
                RequestBody.create(MediaType.parse("application/json"), json)
            )
            pollResponse
        }

    suspend fun refreshAccessToken(body: TokenRefresh) =

        withContext(Dispatchers.IO) {

            val mapper = ObjectMapper() //jackson mapper so we can send a request body in JSON

            val json = mapper.writeValueAsString(body)

            val refreshResponse = webAPI.postWithBody(
                URI(CORE_POLL_LINK),
                "application/json",
                PollResponse::class.java,
                RequestBody.create(MediaType.parse("application/json"), json)
            )
            refreshResponse
        }

    suspend fun revokeAccessToken(body: TokenRevoke) =

        withContext(Dispatchers.IO){

            val mapper = ObjectMapper() //jackson mapper so we can send a request body in JSON

            val json = mapper.writeValueAsString(body)

            val revokeResponse = webAPI.revokeAccessToken(
                RequestBody.create(MediaType.parse("application/json"), json)
            )
            revokeResponse
        }
}