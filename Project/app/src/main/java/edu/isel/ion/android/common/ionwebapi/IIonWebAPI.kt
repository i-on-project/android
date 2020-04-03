package edu.isel.ion.android.common.ionwebapi

import java.net.URI

interface IIonWebAPI {

    suspend fun <T>getFromURI(uri : URI, parseTo : Class<T>) : T

}