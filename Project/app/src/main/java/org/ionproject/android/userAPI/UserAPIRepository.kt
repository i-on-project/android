package org.ionproject.android.userAPI

import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.ionproject.android.common.ionwebapi.AUTH_METHODS_LINK
import org.ionproject.android.common.ionwebapi.CORE_POLL_LINK
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.offline.linkToCatalogProgrammesList
import org.ionproject.android.offline.models.CatalogProgrammes
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.SelectedMethod
import org.ionproject.android.userAPI.models.SelectedMethodResponse
import java.net.URI

class UserAPIRepository(private val webAPI: IIonWebAPI)  {

    suspend fun getAuthMethods() =
        withContext(Dispatchers.IO) {

            val availableMethods = webAPI.getAuthMethodsArray(
                URI(AUTH_METHODS_LINK),
                "application/json"
            )

            availableMethods
        }

    suspend fun loginWithEmail(body: SelectedMethod) =

        withContext(Dispatchers.IO){

            val mapper = ObjectMapper() //jackson mapper so we can send a request body in JSON

            val json = mapper.writeValueAsString(body)

            val loginResponse = webAPI.loginWithEmail(
                URI(AUTH_METHODS_LINK),
                "application/json",
                SelectedMethodResponse::class.java,
                RequestBody.create(MediaType.parse("application/json"), json)
            )
            loginResponse
        }

    suspend fun pollCoreForAuthentication() =
        withContext(Dispatchers.IO){
            var pollResponse = webAPI.getFromURIWithoutAuth(
                URI(CORE_POLL_LINK),
                PollResponse::class.java,
                "application/json"
            )

            pollResponse
        }
}