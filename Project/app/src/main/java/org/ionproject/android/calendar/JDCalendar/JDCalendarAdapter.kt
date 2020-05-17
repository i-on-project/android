package org.ionproject.android.calendar.JDCalendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.EventsListViewModel
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.model.Exam
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.Todo

typealias dayOfMonthClickListener = ((Events) -> Unit)?

/**
 * Default [CalendarAdapter] for the JDCalendar
 *
 * Exposes two constructors, one for when the user wants to add click listener to the month days.
 */
class JDCalendarAdapter() : CalendarAdapter<JDCalendarAdapter.JDViewHolder>() {

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
        return JDViewHolder(view)
    }

    override fun onBindViewHoler(viewHolder: JDViewHolder, day: Day, position: Int) {
        viewHolder.bind(day, events, listener)
    }

    class JDViewHolder(
        view: View
    ) : CalendarAdapter.ViewHolder(view) {

        private val weekDays = WeekDay.values()
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
                val today = day.value.day
                events.forEach {
                    lectures.addAll(findLecturesForThisDay(it.lectures, day))
                    exams.addAll(findExamsForThisDay(it.exams, day))
                    todos.addAll(findTodosForThisDay(it.todos, day))
                }
            }

            monthDayOnClick?.let { onClick ->
                view.setOnClickListener {
                    onClick(Events(exams, lectures, todos))
                }
            }
        }

        private fun findLecturesForThisDay(lectures: List<Lecture>, day: Day): List<Lecture> {
            val weekDay = weekDays[day.value.dayOfWeek - 1].toString()

            return lectures.filter {
                var added = false
                if (it.endDate != null && it.weekDay == weekDay && day.isBefore(it.endDate)) {
                    val color = eventImageView.colorFilter
                    eventImageView.setColorFilter(Color.parseColor(if (color != null) "purple" else Lecture.color))
                    added = true
                }
                added
            }
        }

        private fun findExamsForThisDay(exams: List<Exam>, day: Day): List<Exam> =
            exams.filter {
                var added = false
                if (day.equals(it.startDate)) {
                    val color = eventImageView.colorFilter
                    eventImageView.setColorFilter(Color.parseColor(if (color != null) "purple" else Exam.color))
                    added = true
                }
                added
            }

        private fun findTodosForThisDay(todos: List<Todo>, day: Day): List<Todo> =
            todos.filter {
                var added = false
                if (day.equals(it.due)) {
                    val color = eventImageView.colorFilter
                    eventImageView.setColorFilter(Color.parseColor(if (color != null) "purple" else Todo.color))
                    added = true
                }
                added
            }
    }
}

enum class WeekDay(val extended: String) {
    SU("Sunday"),
    MO("Monday"),
    TU("Thursday"),
    WE("Wednesday"),
    TH("Thursay"),
    FR("Friday"),
    SA("Saturday")
}
