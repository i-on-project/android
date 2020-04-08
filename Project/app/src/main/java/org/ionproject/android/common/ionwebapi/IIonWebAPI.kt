package org.ionproject.android.common.ionwebapi

import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

interface IIonWebAPI {

    /**
     * This is used when the class to parse is generic type
     */
    suspend fun <T> getFromURI(uri: URI): SirenEntity<T>

}