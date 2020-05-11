package org.ionproject.android.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.ionproject.android.R
import org.ionproject.android.TAG
import org.ionproject.android.calendar.JDCalendar.JDCalendarAdapter
import org.ionproject.android.common.model.Events

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    /**
     * Obtaining Calendar's View Model
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            CalendarViewModelProvider()
        )[CalendarViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Improve this, getFavorites then getEvents
        viewModel.getFavoriteClassesFromCurrentTerm()
        viewModel.observeFavorites(this) {
            viewModel.getEvents(it)
            viewModel.observeEvents(this) { events ->
                createCalendar(events)
            }
        }

        val jdCalendar = jdcalendar_calendar

        val calendarAdapter = JDCalendarAdapter { day, view, ImageView ->
            Toast.makeText(context, "Clicked day ${day.value}!", Toast.LENGTH_SHORT).show()
        }

        jdCalendar.adapter = calendarAdapter
    }

    private fun createCalendar(events: List<Events>) {
        Log.v(TAG, "Events = $events")
    }

}
