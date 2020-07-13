package org.ionproject.android.common.ionwebapi

import org.ionproject.android.common.dto.APPLICATION_TYPE
import org.ionproject.android.common.dto.SIREN_SUBTYPE
import java.net.URI

private const val SIREN_MEDIA_TYPE = "$APPLICATION_TYPE/$SIREN_SUBTYPE"
private const val JSON_HOME_MEDIA_TYPE = "$APPLICATION_TYPE/json-home"

interface IIonWebAPI {

    /**
     * This method should perform an http request to the i-on Web API and parse the response to
     * Siren Entity
     */
    suspend fun <T> getFromURI(uri: URI, klass: Class<T>, accept: String = SIREN_MEDIA_TYPE): T

}