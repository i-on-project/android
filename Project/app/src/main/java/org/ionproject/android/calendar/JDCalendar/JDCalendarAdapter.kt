package org.ionproject.android.calendar.JDCalendar

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.TAG
import org.ionproject.android.common.model.Events

typealias MonthDayClickListener = ((Day, View, ImageView) -> Unit)?

/**
 * Default [CalendarAdapter] for the JDCalendar
 *
 * Exposes two constructors, one for when the user wants to add click listener to the month days.
 */
class JDCalendarAdapter() : CalendarAdapter<JDCalendarAdapter.JDViewHolder>() {

    private var events: List<Events> = emptyList()
    private var monthDayClickListener: MonthDayClickListener = null

    /**
     * Registers a listener for the OnClickEvent off all days of the month
     */
    constructor(events: List<Events>, monthDayClickListener: MonthDayClickListener) : this() {
        this.events = events
        this.monthDayClickListener = monthDayClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup): JDViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_jdcalendar, parent, false)
        return JDViewHolder(view, events, monthDayClickListener)
    }

    class JDViewHolder(
        view: View,
        val events: List<Events>,
        private val monthDayOnClick: MonthDayClickListener
    ) : CalendarAdapter.ViewHolder(view) {

        val dayTextView =
            view.textview_list_item_calendar_day //Contains the text of the day (e.g 1,2..)
        val eventImageView = view.imageview_list_item_calendar_event //The event circle small circle

        override fun bind(day: Day) {
            view.setBackgroundResource(0) //Cleans background resource
            var textColor = Color.BLACK //Default text color

            if (day.isToday) { //If the day is today update the text color and background
                textColor = Color.WHITE
                view.setBackgroundResource(R.drawable.shape_circle_30dp)
            } else if (!day.isDayOfCurrMonth) //If the day is not from this month update the text color
                textColor = Color.LTGRAY

            dayTextView.setTextColor(textColor)
            dayTextView.text = "${day.value.day}"

            findLecturesForThisDay(day.value.weekDayShortName())

            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(day, view, eventImageView)
                }
            }
        }

        private fun findLecturesForThisDay(weekDay: String) {
            for (event in events) {
                for(lecture in event.lectures) {
                    Log.v(TAG,"Lecture.weekDay = ${lecture.weekDay} e WeekDay = $weekDay")
                    if(lecture.weekDay == weekDay)
                        Log.v(TAG, "Week Day = $weekDay")
                }
            }
        }
    }
}