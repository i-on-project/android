package org.ionproject.android

import kotlinx.coroutines.runBlocking
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.JsonHome
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IonService
import org.ionproject.android.common.ionwebapi.IonWebAPI
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.common.toCalendarTermList
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.URI

class WebAPITests {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://host1.dev.ionproject.org")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: IonService = retrofit.create(IonService::class.java)
    private val webAPI = IonWebAPI(service, JacksonIonMapper())

    @Test
    fun shouldGetRootResource() {
        val jsonHome: JsonHome = runBlocking {
            webAPI.getFromURI(URI("/"), JsonHome::class.java)
        }
        assert(jsonHome.api.title == "i-on Core")
        assert(jsonHome.resources.size > 0)
    }

    @Test
    fun shouldGetAllCalendarTerms() {
        val calendarTerms = runBlocking {
            webAPI.getFromURI(URI("/v0/calendar-terms"), SirenEntity::class.java)
                .toCalendarTermList()
        }

        assert(calendarTerms.isNotEmpty())
        assert(calendarTerms.find { it.year == 1718 } != null)
    }

    @Test
    fun shouldGetAllClassesForTerm1920V() {
        val response = runBlocking {
            webAPI.getFromURI(URI("/v0/calendar-terms/1920v"), SirenEntity::class.java)
        }

        assert(response.entities != null)
        assert(response.entities?.size == 3)
    }

}
