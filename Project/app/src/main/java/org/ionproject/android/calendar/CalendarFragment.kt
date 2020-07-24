package org.ionproject.android.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading

class CalendarFragment : ExceptionHandlingFragment() {
    /**
     * Obtaining Calendar's View Model
     */
    private val calendarViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CalendarViewModelProvider()
        )[CalendarViewModel::class.java]
    }

    /**
     * Obtaining Events List's View Model
     */
    private val eventsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
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

        setupEventsList()

        // Hide calendar, show progress bar
        val viewGroup = jdcalendar_calendar.parent as ViewGroup
        viewGroup.startLoading()

        calendarViewModel.getAllEventsFromFavorites { events ->
            viewGroup.stopLoading()
            createCalendarWithEvents(events)
        }
    }

    private fun setupEventsList() {
        val eventsList = recyclerView_calendar_events_list
        eventsList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        eventsList.layoutManager = LinearLayoutManager(context)
        eventsList.adapter = eventsListAdapter
    }

    /**
     * This should initialize the Calendar Adapter with all the events found for all
     * favorites classes
     */
    private fun createCalendarWithEvents(events: List<Events>) {
        val calendarAdapter = IonCalendarAdapter(
            eventsListViewModel,
            events
        ) {
            eventsListViewModel.reset() // clear the events that are being shown
            eventsListViewModel.addEvents(it) // add the new events to be shown
            eventsListAdapter.notifyDataSetChanged() // notify the eventList's adapter to show the new events
        }
        jdcalendar_calendar.adapter = calendarAdapter
        jdcalendar_calendar.onMonthChangeListener = {
            eventsListViewModel.reset()
            eventsListAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        jdcalendar_calendar.destroy()
    }

}
