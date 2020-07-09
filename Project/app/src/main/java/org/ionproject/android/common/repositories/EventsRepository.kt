package org.ionproject.android.common.repositories

import org.ionproject.android.common.dto.SirenICalendar
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.model.toEventsSummary
import java.net.URI

/**
 * Repository to search about events
 * This events can be exams, a meeting or an appointment.
 */
class EventsRepository(private val ionWebAPI: IIonWebAPI) {
    /**
     * This should return all events available for a class
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Events which contains all [Lectures],[Exams],[Todos] and [Journals] available
     */
    suspend fun getEvents(uri: URI): Events =
        ionWebAPI
            .getFromURI(uri, SirenICalendar::class.java)
            .toEventsSummary()
}