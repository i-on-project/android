package org.ionproject.android.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.jdcalendar.CalendarAdapter
import org.ionproject.android.calendar.jdcalendar.Day
import org.ionproject.android.calendar.jdcalendar.day
import org.ionproject.android.calendar.jdcalendar.dayOfWeek
import org.ionproject.android.common.model.*

typealias dayOfMonthClickListener = ((Events) -> Unit)?

/**
 * Default [CalendarAdapter] for the JDCalendar
 *
 * Exposes two constructors, one for when the user wants to add click listener to the month days.
 */
class IonCalendarAdapter() : CalendarAdapter<IonCalendarAdapter.JDViewHolder>() {

    private var viewModel: EventsListViewModel? = null
    private var events: List<Events> = emptyList()
    private var listener: dayOfMonthClickListener = null

    /**
     * Registers a listener for the OnClickEvent off all days of the month
     */
    constructor(
        viewModel: EventsListViewModel,
        events: List<Events>,
        listener: dayOfMonthClickListener
    ) : this() {
        this.viewModel = viewModel
        this.events = events
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
        viewHolder.bind(day, events, listener)
    }

    class JDViewHolder(
        view: View
    ) : ViewHolder(view) {

        private val dayTextView =
            view.textview_list_item_calendar_day //Contains the text of the day (e.g 1,2..)
        private val eventImageView =
            view.imageview_list_item_calendar_event //The event small circle

        fun bind(day: Day, events: List<Events>, monthDayOnClick: dayOfMonthClickListener) {
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

            val lectures = mutableListOf<Lecture>()
            val exams = mutableListOf<Exam>()
            val todos = mutableListOf<Todo>()

            if (day.isToday || day.isAfterToday) {
                events.forEach {
                    lectures.addAll(findLecturesForThisDay(it.lectures, day))
                    exams.addAll(findExamsForThisDay(it.exams, day))
                    todos.addAll(findTodosForThisDay(it.todos, day))
                }
            }

            // show all events available for this specific day
            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(Events(exams, lectures, todos, journals = emptyList()))

                }
            }
        }

        private fun findLecturesForThisDay(lectures: List<Lecture>, day: Day): List<Lecture> {
            val weekDay = WeekDay.byNumber(day.value.dayOfWeek)

            return lectures.filter {
                if (it.weekDay == weekDay && day.isBefore(it.endDate)) {
                    val currColor = eventImageView.colorFilter
                    val color = Color.parseColor(if (currColor != null) "purple" else Lecture.color)
                    eventImageView.setColorFilter(color)
                    return@filter true
                }
                return@filter false
            }

        }

        private fun findExamsForThisDay(exams: List<Exam>, day: Day): List<Exam> =
            exams.filter {
                var added = false
                if (day.equals(it.startDate)) {
                    val currColor = eventImageView.colorFilter
                    val color = Color.parseColor(if (currColor != null) "purple" else Exam.color)
                    eventImageView.setColorFilter(color)
                    added = true
                }
                added
            }

        private fun findTodosForThisDay(todos: List<Todo>, day: Day): List<Todo> =
            todos.filter {
                var added = false
                if (day.equals(it.due)) {
                    val currColor = eventImageView.colorFilter
                    val color = Color.parseColor(if (currColor != null) "purple" else Todo.color)
                    eventImageView.setColorFilter(color)
                    added = true
                }
                added
            }
    }
}
