package org.ionproject.android.common.repositories

import org.ionproject.android.common.ExamSummary
import org.ionproject.android.common.Lecture
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.siren.Event
import org.ionproject.android.common.siren.ICalendar
import org.ionproject.android.common.toCalendarSummary
import org.ionproject.android.common.toExamSummary
import java.net.URI

/**
 * Repository to search about events
 * This events can be exams, a meeting or an appointment.
 */
class EventsRepository(
    private val ionWebAPI: IIonWebAPI
) {
    /**
     * This should return all exams available for a class
     */
    suspend fun getExamFromCourse(uri: URI): ExamSummary {
        return ionWebAPI
            .getFromURI(uri, Event::class.java)
            .toExamSummary()
    }

    /**
     * This should return all lectures available for a class section
     */
    suspend fun getLectures(calendarURI: URI?): List<Lecture> {
        if (calendarURI == null)
            return emptyList()
        return ionWebAPI
            .getFromURI(calendarURI, ICalendar::class.java)
            .toCalendarSummary()
    }
}