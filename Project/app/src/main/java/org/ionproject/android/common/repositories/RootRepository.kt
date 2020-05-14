package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.dto.JsonHome
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import java.net.URI


// This uri has to be hardcoded there is no other way
private val ROOT_URI_V0 = URI("/V0")

/**
 * Used to check the existence of i-on core Web API root endpoints. It does so by
 * checking the Root resource.
 */
class RootRepository(private val ionWebAPI: IIonWebAPI) {

    private var jsonHome: JsonHome? = null

    suspend fun getJsonHome() = jsonHome?: withContext(Dispatchers.IO)
        {
            jsonHome = ionWebAPI.getFromURI(ROOT_URI_V0, JsonHome::class.java)
        }
}