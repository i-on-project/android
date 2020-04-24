package org.ionproject.android.common.ionwebapi

import org.ionproject.android.common.siren.SirenEntity

interface IIonMapper {

    /**
    This method should parse a String response from
    the i-on Web API to a [SirenEntity]
     */
    suspend fun <T> parse(responseBody: String, klass: Class<T>): T

}
