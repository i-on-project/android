package org.ionproject.android.common.repositories

import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.siren.SirenEntity
import java.net.URI

/**
 * Repository to search about events
 * This events can be exams, a meeting or an appointment.
 */
class EventsRepository(
    private val ionWebAPI: IIonWebAPI
) {

    //TODO: Create more methods to get all Exams from a calendar term
    /**
     * This will be a test to get 1st PG Exam from semester 1920v
     */
    suspend fun getFirstPGExam(uri: URI): SirenEntity {
        return ionWebAPI.getFromURI(uri)
    }
}