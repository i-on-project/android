package org.ionproject.android.common.repositories

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.EventsDao
import org.ionproject.android.common.dto.SirenICalendar
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.ionwebapi.WEB_API_HOST
import org.ionproject.android.common.model.*
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
    private val workerManagerFacade: WorkerManagerFacade,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * Obtains all events directly from the API or from the local Db
     * from the resource with [uri]
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Events which contains all [Lectures],[Exams],[Todos] and [Journals] available
     */
    suspend fun getEvents(uri: URI) = withContext(dispatcher) {
        var events = eventsDao.getEventsByUri(URI("http://localhost:10023${uri.path}"))

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
    suspend fun forceGetEvents(uri: URI) = withContext(dispatcher) {
        val eventsLocal = eventsDao.getEventsByUri(URI("http://localhost:10023${uri.path}"))
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

    /**
     * Gets all [Lecture] events from a [ClassSection] and also all
     * [Exam], [Journal] and [Todo] from a [Class].
     *
     * @param classSection The class section to get events from
     * @param getEvents Method which should return all events (In this case getEvents or forceGetEvents from this class)
     * @param getClassCollectionByUri Method which should return a class collection, in order to get it's events
     *
     * @return Events Object which contains all [Lecture], [Exam], [Journal] and [Todo] events.
     */
    suspend fun getAllEventsFromClassSection(
        classSection: ClassSection,
        getEvents: suspend (URI) -> Events?,
        getClassCollectionByUri: suspend (URI) -> ClassCollection?
    ): Events {
        val classCollection = getClassCollectionByUri(classSection.upURI)

        val exams = mutableListOf<Exam>()
        val lectures = mutableListOf<Lecture>()
        val todos = mutableListOf<Todo>()
        val journals = mutableListOf<Journal>()

        suspend fun getEventsAndAddToLists(uri: URI?) {
            if (uri != null && uri.path != "") {
                val events = getEvents(uri)
                events?.apply {
                    exams.addAll(events.exams)
                    lectures.addAll(events.lectures)
                    todos.addAll(events.todos)
                    journals.addAll(events.journals)
                }
            }
        }

        if (classCollection != null)
            getEventsAndAddToLists(URI("$WEB_API_HOST${classCollection.fields.calendarURI?.path}"))
        getEventsAndAddToLists(URI("$WEB_API_HOST${classSection.calendarURI?.path}"))

        return Events.create(exams, lectures, todos, journals)
    }

}