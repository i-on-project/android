package edu.isel.ion.android.common.ionwebapi

import com.fasterxml.jackson.core.type.TypeReference
import java.net.URI

interface IIonWebAPI {

    suspend fun <T>getFromURI(uri : URI, clazz : Class<T>) : T

    /*
        This is used when the class to parse is generic type
     */
    suspend fun <T>getFromURI(uri : URI, typeReference : TypeReference<T>) : T

}