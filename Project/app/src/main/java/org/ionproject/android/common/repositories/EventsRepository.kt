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
     * Obtains all events directly from the API or from the local Db
     * from the resource with [uri]
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
            eventsDao.insertEvents(events)
        } else {
            workerManagerFacade.resetWorkerJobsByCacheable(events.fields)
        }
        events
    }

    /**
     * Obtains all events directly from the API and NEVER from the local Db
     * from the resource with [uri]
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Events which contains all [Lectures],[Exams],[Todos] and [Journals] available
     */
    suspend fun forceGetEvents(uri: URI) = withContext(Dispatchers.IO) {
        val eventsLocal = eventsDao.getEventsByUri(uri)
        val eventsServer = ionWebAPI.getFromURI(uri, SirenICalendar::class.java).toEventsSummary()

        if (eventsLocal == null) {
            val workerId = workerManagerFacade.enqueueWorkForEvents(
                eventsServer,
                WorkImportance.VERY_IMPORTANT
            )
            eventsServer.fields.workerId = workerId
        } else {
            eventsServer.fields.workerId = eventsLocal.fields.workerId
            eventsDao.deleteEventsByUri(eventsLocal.fields.selfUri)
            workerManagerFacade.resetWorkerJobsByCacheable(eventsServer.fields)
        }
        eventsDao.insertEvents(eventsServer)

        eventsServer
    }

}