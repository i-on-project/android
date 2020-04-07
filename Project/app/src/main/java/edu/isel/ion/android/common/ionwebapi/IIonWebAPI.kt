package edu.isel.ion.android.common.ionwebapi

import com.fasterxml.jackson.core.type.TypeReference
import edu.isel.ion.android.common.SirenEntity
import java.net.URI

interface IIonWebAPI {

    /*
        This is used when the class to parse is generic type
     */
    suspend fun <T>getFromURI(uri : URI) : SirenEntity<T>

    suspend fun <T>getFromURI(uri : URI,  clazz : Class<T>) : T

}