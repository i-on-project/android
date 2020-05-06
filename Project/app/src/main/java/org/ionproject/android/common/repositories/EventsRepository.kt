package org.ionproject.android.common.repositories

import org.ionproject.android.common.dtos.*
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.siren.Event
import org.ionproject.android.common.siren.ICalendar
import org.ionproject.android.common.siren.Journal
import org.ionproject.android.common.siren.Todo
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
    suspend fun getExamFromCourse(uri: URI): ExamSummary {
        return ionWebAPI
            .getFromURI(uri, Event::class.java)
            .toExamSummary()
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
            .getFromURI(calendarURI, ICalendar::class.java)
            .toCalendarSummary()
    }

    /**
     * This should return a work assignment for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Work Assignment information collected from the Web API
     */
    suspend fun getWorkAssignment(uri: URI): TodoSummary =
        ionWebAPI
            .getFromURI(uri, Todo::class.java)
            .toTodoSummary()

    /**
     * This should return a journal available for a class section
     *
     * @param uri URI to make a request to the Web API
     *
     * @return Journal information collected from the Web API
     */
    suspend fun getJournals(uri: URI): JournalSummary =
        ionWebAPI
            .getFromURI(uri, Journal::class.java)
            .toJournalSummary()
}