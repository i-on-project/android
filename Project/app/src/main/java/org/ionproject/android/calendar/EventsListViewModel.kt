package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.Event
import org.ionproject.android.common.model.Events

class EventsListViewModel : ViewModel() {

    private val eventsList = mutableListOf<Event>()

    val events: List<Event>
        get() = eventsList

    fun reset() = eventsList.clear()

    fun addEvents(events: Events) {
        eventsList.addAll(events.exams)
        eventsList.addAll(events.lectures)
        eventsList.addAll(events.todos)
    }
}