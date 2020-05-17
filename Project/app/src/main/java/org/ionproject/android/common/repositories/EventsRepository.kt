package org.ionproject.android.common.repositories

import org.ionproject.android.common.dto.EventDto
import org.ionproject.android.common.dto.ICalendarDto
import org.ionproject.android.common.dto.JournalDto
import org.ionproject.android.common.dto.TodoDto
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.*
import java.net.URI

/**
 * Repository to search about events
 * This events can be exams, a meeting or an appointment.
 */
class EventsRepository(
    private val ionWebAPI: IIonWebAPI
) {
    /**
     * This should return a exam information available for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Exam information collected from the Web API
     */
    suspend fun getExamFromCourse(uri: URI): Exam {
        return ionWebAPI
            .getFromURI(uri, EventDto::class.java)
            .toExam()
    }

    /**
     * This should return all lectures available for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return List of lectures information collected from the Web API
     */
    suspend fun getLectures(calendarURI: URI?): List<Lecture> {
        if (calendarURI == null)
            return emptyList()
        return ionWebAPI
            .getFromURI(calendarURI, ICalendarDto::class.java)
            .getAllLectures()
    }

    /**
     * This should return a work assignment for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Work Assignment information collected from the Web API
     */
    suspend fun getWorkAssignment(uri: URI): Todo =
        ionWebAPI
            .getFromURI(uri, TodoDto::class.java)
            .toTodoSummary()

    /**
     * This should return a journal available for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Journal information collected from the Web API
     */
    suspend fun getJournals(uri: URI): Journal =
        ionWebAPI
            .getFromURI(uri, JournalDto::class.java)
            .toJournal()

    /**
     * This should return all events available for a class
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Events which contains all [Lectures],[Exams],[Todos] and [Journals] available
     */
    suspend fun getEvents(uri: URI): Events =
        ionWebAPI
            .getFromURI(uri, ICalendarDto::class.java)
            .toEventsSummary()
}