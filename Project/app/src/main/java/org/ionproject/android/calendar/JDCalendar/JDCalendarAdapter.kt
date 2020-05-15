package org.ionproject.android.calendar.JDCalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.model.Exam
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.Todo

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
        return JDViewHolder(view)
    }

    override fun onBindViewHoler(viewHolder: JDViewHolder, day: Day, position: Int) {
        viewHolder.bind(day, events, monthDayClickListener)
    }

    class JDViewHolder(
        view: View
    ) : CalendarAdapter.ViewHolder(view) {

        private val weekDays = WeekDay.values()
        private val dayTextView =
            view.textview_list_item_calendar_day //Contains the text of the day (e.g 1,2..)
        private val eventImageView =
            view.imageview_list_item_calendar_event //The event small circle

        fun bind(day: Day, events: List<Events>, monthDayOnClick: MonthDayClickListener) {
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

            if (day.isToday || day.isAfterToday) {
                events.forEach {
                    findLecturesForThisDay(it.lectures, day)
                    findExamsForThisDay(it.exams, day)
                    findTodosForThisDay(it.todos, day)
                }
            }

            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(day, view, eventImageView)
                }
            }
        }

        private fun findLecturesForThisDay(lectures: List<Lecture>, day: Day) {
            val weekDay = weekDays[day.value.dayOfWeek-1].toString()

            lectures.forEach {
                if (it.endDate != null && it.weekDay == weekDay && day.isBefore(it.endDate)) {
                    eventImageView.setColorFilter(Color.parseColor("blue"))
                }
            }
        }

        private fun findExamsForThisDay(exams: List<Exam>, day: Day) {
            exams.forEach {
                if (day.equals(it.startDate)) {
                    eventImageView.setColorFilter(Color.parseColor("red"))
                }
            }
        }

        private fun findTodosForThisDay(todos: List<Todo>, day: Day) {
            todos.forEach {
                if(day.equals(it.due))
                    eventImageView.setColorFilter(Color.parseColor("#FF8C00"))
            }
        }
    }
}

private enum class WeekDay {
    SU, MO, TU, WE, TH, FR, SA
}
