package org.ionproject.android.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.JDCalendar.CalendarAdapter
import org.ionproject.android.calendar.JDCalendar.Day

typealias MonthDayClickListener = ((Day, View) -> Unit)?

class JDCalendarAdapter() : CalendarAdapter<JDCalendarAdapter.JDViewHolder>() {

    /**
     * Registers a listener for the OnClickEvent off all days of the month
     */
    constructor(monthDayClickListener: MonthDayClickListener) : this() {
        this.monthDayClickListener = monthDayClickListener
    }

    private var monthDayClickListener: MonthDayClickListener = null

    override fun onCreateViewHolder(parent: ViewGroup): JDViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_jdcalendar, parent, false)
        return JDViewHolder(view, monthDayClickListener)
    }

    class JDViewHolder(private val view: View, private val monthDayOnClick: MonthDayClickListener) :
        CalendarAdapter.ViewHolder(view) {

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
            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(day, view)
                }
            }
        }
    }
}