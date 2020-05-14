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
import org.ionproject.android.calendar.JDCalendar.Day
import org.ionproject.android.calendar.JDCalendar.JDCalendar
import org.ionproject.android.calendar.JDCalendar.JDCalendarAdapter
import org.ionproject.android.calendar.JDCalendar.getWeekDay
import org.ionproject.android.common.model.Events

class CalendarFragment : Fragment() {

    /**
     * Obtaining Calendar's View Model
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            CalendarViewModelProvider()
        )[CalendarViewModel::class.java]
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
        viewModel.apply {
            getFavoriteClassesFromCurrentTerm(this@CalendarFragment) { classesSummary ->
                getEvents(classesSummary) { events ->
                    createCalendarWithEvents(events)
                }
            }
        }
    }

    private fun createCalendarWithEvents(events: List<Events>) {
        val calendarAdapter = JDCalendarAdapter(events) { day, view, ImageView ->
            Toast.makeText(context, "Clicked day ${day.value}!", Toast.LENGTH_SHORT)
                .show()
        }

        jdCalendar.adapter = calendarAdapter
    }
}
