package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.EventsDao
import org.ionproject.android.common.dto.SirenICalendar
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.toEventsSummary
import org.ionproject.android.common.workers.WorkImportance
import org.ionproject.android.common.workers.WorkerManagerFacade
import java.net.URI

/**
 * Repository to search about events
 * This events can be exams, a meeting or an appointment.
 */
class EventsRepository(
    private val eventsDao: EventsDao,
    private val ionWebAPI: IIonWebAPI,
    private val workerManagerFacade: WorkerManagerFacade
) {
    /**
     * This should return all events available for a class
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Events which contains all [Lectures],[Exams],[Todos] and [Journals] available
     */
    suspend fun getEvents(uri: URI) = withContext(Dispatchers.IO) {
        var events = eventsDao.getEventsByUri(uri)

        if (events == null) {
            events = ionWebAPI.getFromURI(uri, SirenICalendar::class.java).toEventsSummary()
            val workerId = workerManagerFacade.enqueueWorkForEvents(
                events,
                WorkImportance.VERY_IMPORTANT
            )
            events.fields.workerId = workerId
        } else {
            workerManagerFacade.resetWorkerJobsByCacheable(events.fields)
        }
        events
    }

}