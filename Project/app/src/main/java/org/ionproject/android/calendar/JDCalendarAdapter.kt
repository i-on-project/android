package org.ionproject.android.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.JDCalendar.CalendarAdapter
import org.ionproject.android.calendar.JDCalendar.Day

class JDCalendarAdapter : CalendarAdapter<JDCalendarAdapter.JDViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): JDViewHolder {
        //TODO Check the attach to root parameter
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_jdcalendar, parent, false)
        return JDViewHolder(view)
    }

    class JDViewHolder(view: View) : CalendarAdapter.ViewHolder(view) {

        val dayTextView = view.textview_list_item_calendar_day
        val eventImageView = view.imageview_list_item_calendar_event

        override fun bind(day: Day) {
            view.setBackgroundResource(0) //Cleans background resource
            var textColor = Color.BLACK
            if (day.isToday) {
                textColor = Color.WHITE
                view.setBackgroundResource(R.drawable.shape_circle_30dp)
            } else if (!day.isDayOfCurrMonth)
                textColor = Color.LTGRAY
            dayTextView.setTextColor(textColor)
            dayTextView.text = "${day.value}"
        }
    }
}