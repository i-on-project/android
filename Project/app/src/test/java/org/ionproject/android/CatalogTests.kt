package org.ionproject.android

import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.net.URI
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import org.ionproject.android.common.ionwebapi.IonService
import org.ionproject.android.common.ionwebapi.IonWebAPI
import org.ionproject.android.common.ionwebapi.JacksonIonMapper
import org.ionproject.android.offline.*
import org.ionproject.android.offline.models.*

class CatalogTests {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    private val service: IonService = retrofit.create(IonService::class.java)
    private val webAPI = IonWebAPI(service, JacksonIonMapper())

    private val catalogRepository = CatalogRepository(webAPI)

    @Test
    fun getCatalogProgrammeListTest() {

        val programmeList: CatalogProgrammes = runBlocking {
            webAPI.getFromURI(
                URI("/repos/i-on-project/integration-data/git/trees/748698e9248d2bb20a2266cb78b386a37f39930d"),
                CatalogProgrammes::class.java,
                "application/json"
            )
        }

        println("programmeLIst: $programmeList")
        assertEquals(programmeList.programmes.size, 1)

    }

    @Test
    fun getCatalogProgrammeTest(){

        val programme: CatalogProgrammeTerms = runBlocking {
            webAPI.getFromURI(
                URI("/repos/i-on-project/integration-data/git/trees/89b8c8474fce12b6729138ecc48c22f1755719f1"),
                CatalogProgrammeTerms::class.java,
                "application/json"
            )
        }

        println("programme: $programme")
        //assertEquals(programme.size, 1)
        assertEquals(programme.terms[0].term, "2020-2021-2")
    }

    @Test
    fun getTermFilesTest(){

        val termFileList: CatalogProgrammeTermInfo = runBlocking {
            webAPI.getFromURI(
                URI("/repos/i-on-project/integration-data/git/trees/8dd752ff6f401036e425cc7d59ee4b17bd841d31"),
                CatalogProgrammeTermInfo::class.java,
                "application/json"
            )
        }

        println("programmeLIst: $termFileList")
        assertEquals(termFileList.files.size, 4)

    }

    @Test
    fun getCatalogCalendar(){

        val calendar = runBlocking {

            val link = linkToCalendar.format("2020-2021")

            println(link)

            webAPI.getFromURI(URI(link), CatalogCalendar::class.java, "application/json")

        }

        println("$calendar")
        assertEquals("2019-2020-1", calendar.terms[0].calendarTerm)

    }

    @Test
    fun testCatalogRepository(){

        val programmeList = runBlocking{
            catalogRepository.getCatalogProgrammeList()
        }

        println("programmeLIst: $programmeList")
        if (programmeList != null) {
            assertEquals(programmeList.programmes.size, 1)
        }


        val programmeTerms = runBlocking {
            println(programmeList!!.programmes[0].programmeName)
            catalogRepository.getCatalogProgrammeTerms(URI(programmeList!!.programmes[0].linkToInfo))
        }

        println("Term list : $programmeTerms")
        if (programmeTerms != null) {
            assertEquals(programmeTerms.terms.size, 1)
        }

        val termFileList = runBlocking {
            println(programmeTerms!!.terms[0].term)
            catalogRepository.getTermInfo(URI(programmeTerms.terms[0].linkToInfo))
        }

        println("File list : $termFileList")
        assertEquals(termFileList.size, 2)

        val examSchedule = runBlocking {

            catalogRepository.getFileFromGithub(programmeList!!.programmes[0].programmeName, programmeTerms!!.terms[0].term,
                ExamSchedule::class.java)

        }

        println(examSchedule)
        assertEquals(examSchedule.school.name, "Instituto Superior Engenharia Lisboa")

        val timetable = runBlocking {

            catalogRepository.getFileFromGithub(programmeList!!.programmes[0].programmeName, programmeTerms!!.terms[0].term,
                Timetable::class.java)

        }

        println(timetable)
        assertEquals(timetable.school.name, "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA")
    }
}