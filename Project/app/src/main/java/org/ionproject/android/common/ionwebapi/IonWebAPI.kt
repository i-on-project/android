package org.ionproject.android.common.ionwebapi

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import org.ionproject.android.userAPI.models.AuthMethod
import org.ionproject.android.userAPI.models.SelectedMethod
import org.ionproject.android.userAPI.models.SelectedMethodResponse
import java.net.URI

class IonWebAPI(private val ionService: IonService, private val ionMapper: IIonMapper) :
    IIonWebAPI {

    /**
     *  This is using the IO Dispacher, which is optimized to perform disk or network I/O
     *  outside of the main thread. Examples include using the Room component,
     *  reading from or writing to files, and running any network operations.
     */
    override suspend fun <T> getFromURI(
        uri: URI,
        klass: Class<T>,
        accept: String
    ): T {
        val response = withContext(Dispatchers.IO) {
            Log.d("API", "Requesting resource from uri $uri")

            ionService.getFromUri(uri.toString(), accept)
        }
        return ionMapper.parse(response, klass)
    }

    /**
     *  General fetch function without Authorization headers
     */
    override suspend fun <T> getFromURIWithoutAuth(
        uri: URI,
        klass: Class<T>,
        accept: String
    ): T {
        val response = withContext(Dispatchers.IO) {
            Log.d("API", "Requesting resource from uri $uri")
            ionService.getFromUriWithoutAuth(uri.toString(), accept)
        }
        return ionMapper.parse(response, klass)
    }

    /**
     * Since the ionMapper needs a class and the Auth Methods request returns an array
     * of JSON objects (instead of an array inside a JSON object), we need to have a
     * special parser
     */
    override suspend fun getAuthMethodsArray(uri: URI, accept: String): List<AuthMethod> {

        val response = withContext(Dispatchers.IO) {
            Log.d("API", "Requesting resource from uri $uri")
            ionService.getFromUriWithoutAuth(uri.toString(), accept)
        }

        return jacksonObjectMapper().readValue(response)
    }

    override suspend fun <T> loginWithEmail(
        uri: URI,
        accept: String,
        klass: Class<T>,
        body: RequestBody
    ): T {
        val response = withContext(Dispatchers.IO) {
            Log.d("API", "Requesting resource from uri $uri")
            ionService.loginWithEmail(uri.toString(), accept, body)
        }
        return ionMapper.parse(response, klass)
    }
}