package org.ionproject.android.common.ionwebapi

import java.net.URI

interface IIonWebAPI {

    /**
     * This method should perform an http request to the i-on Web API and parse the response to
     * Siren Entity
     */
    suspend fun <T> getFromURI(uri: URI, klass: Class<T>): T

}