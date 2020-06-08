package org.ionproject.android.calendar.jdcalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R

typealias dayOfMonthClickListener = ((Day) -> Unit)?

/**
 * Default [CalendarAdapter] for the JDCalendar
 *
 * Exposes two constructors, one for when the user wants to add click listener to the month days.
 */
class JDCalendarAdapter() : CalendarAdapter<JDCalendarAdapter.JDViewHolder>() {

    private var listener: dayOfMonthClickListener = null

    /**
     * Registers a listener for the OnClickEvent off all days of the month
     */
    constructor(
        listener: dayOfMonthClickListener
    ) : this() {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup): JDViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.grid_item_jdcalendar, parent, false)
        return JDViewHolder(
            view
        )
    }

    override fun onBindViewHolder(viewHolder: JDViewHolder, day: Day, position: Int) {
        viewHolder.bind(day, listener)
    }

    class JDViewHolder(
        view: View
    ) : ViewHolder(view) {

        private val dayTextView =
            view.textview_list_item_calendar_day //Contains the text of the day (e.g 1,2..)
        private val eventImageView =
            view.imageview_list_item_calendar_event //The event small circle

        fun bind(day: Day) {
            view.setBackgroundResource(0) //Cleans background resource
            eventImageView.clearColorFilter() //Cleans ImageView resource
            var textColor = Color.BLACK //Default text color

            if (day.isToday) { //If the day is today update the text color and background
                textColor = Color.WHITE
                view.setBackgroundResource(R.drawable.shape_circle_30dp)
            } else if (!day.isDayOfCurrMonth) //If the day is not from this month update the text color
                textColor = Color.LTGRAY

            dayTextView.setTextColor(textColor)
            dayTextView.text = "${day.value.day}"
        }

        fun bind(day: Day, monthDayOnClick: dayOfMonthClickListener) {
            bind(day)
            // show all events available for this specific day
            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(day)
                }
            }
        }
    }
}
