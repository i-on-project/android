package org.ionproject.android.common.ionwebapi

import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

interface IIonWebAPI {

    /**
     * This method should perform an http request to the i-on Web API and parse the response to
     * Siren Entity
     */
    suspend fun getFromURI(uri: URI): SirenEntity

}