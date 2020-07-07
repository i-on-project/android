package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.Event
import org.ionproject.android.common.model.Events

class EventsListViewModel : ViewModel() {

    /**
     * List of events that are being shown to the user
     * This events can be of type [Exam],[Lecture] or [Todo]
     */
    private val eventsList = mutableListOf<Event>()

    val events: List<Event>
        get() = eventsList

    fun reset() = eventsList.clear()

    fun addEvents(events: Events) {
        eventsList.addAll(events.exams + events.lectures + events.todos)
    }
}