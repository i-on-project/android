package org.ionproject.android.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.ionproject.android.R
import org.ionproject.android.calendar.JDCalendar.JDCalendarAdapter
import org.ionproject.android.common.model.Events

class CalendarFragment : Fragment() {
    /**
     * Obtaining Calendar's View Model
     */
    private val calendarViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            CalendarViewModelProvider()
        )[CalendarViewModel::class.java]
    }

    /**
     * Obtaining Events List's View Model
     */
    private val eventsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            EventsListViewModelProvider()
        )[EventsListViewModel::class.java]
    }

    /**
     * Creates the events list's adapter
     */
    private val eventsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        EventsListAdapter(eventsListViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupEventsListAdapter()

        calendarViewModel.apply {
            getFavoriteClassesFromCurrentTerm(this@CalendarFragment) { favorites ->
                getEvents(favorites) { events ->
                    createCalendarWithEvents(events)
                }
            }
        }
    }

    private fun setupEventsListAdapter() {
        val eventsList = recyclerView_calendar_events_list
        eventsList.layoutManager = LinearLayoutManager(context)
        eventsList.adapter = eventsListAdapter
    }

    /**
     * This should initialize the Calendar Adapter with all the events found for all
     * favorites classes
     */
    private fun createCalendarWithEvents(events: List<Events>) {
        val calendarAdapter = JDCalendarAdapter(eventsListViewModel, events) {
            eventsListViewModel.reset() // clear the events that are being shown
            eventsListViewModel.addEvents(it) // add the new events to be shown
            eventsListAdapter.notifyDataSetChanged() // notify the eventList's adapter to show the new events
        }
        jdcalendar_calendar.adapter = calendarAdapter
    }

}
