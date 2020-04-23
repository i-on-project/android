package org.ionproject.android.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_calendar.*
import org.ionproject.android.R

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jdCalendar = jdcalendar_calendar
        val calendarAdapter = JDCalendarAdapter { day, view ->
            Toast.makeText(context, "Clicked day ${day.value}!", Toast.LENGTH_SHORT).show()
        }
        jdCalendar.adapter = calendarAdapter
    }
}
