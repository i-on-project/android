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
import org.ionproject.android.calendar.JDCalendar.JDCalendar
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

    private val eventsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            EventsListViewModelProvider()
        )[EventsListViewModel::class.java]
    }

    private val eventsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        EventsListAdapter(eventsListViewModel)
    }

    private val jdCalendar: JDCalendar by lazy {
        jdcalendar_calendar
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

    private fun createCalendarWithEvents(events: List<Events>) {
        val calendarAdapter = JDCalendarAdapter(eventsListViewModel, events) {
            eventsListViewModel.reset()
            eventsListViewModel.addEvents(it)
            eventsListAdapter.notifyDataSetChanged()
        }
        jdCalendar.adapter = calendarAdapter
    }

}
