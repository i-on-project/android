package org.ionproject.android.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.grid_item_jdcalendar.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.*
import org.ionproject.jdcalendar.CalendarAdapter
import org.ionproject.jdcalendar.Day
import org.ionproject.jdcalendar.day
import org.ionproject.jdcalendar.dayOfWeek

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
    private var selectedDay: JDViewHolder? = null

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

    override fun onBindViewHolder(viewHolder: JDViewHolder, day: Day, position: Int) {
        viewHolder.bind(day, events, listener) {
            val selectedDayLocal = selectedDay
            selectedDayLocal?.apply {
                selectedDayLocal.view
                    .textview_list_item_calendar_day.setTextColor(selectedDayLocal.textColor)
                selectedDayLocal.view
                    .setBackgroundResource(selectedDayLocal.backgroundResource)
            }
            it.view.setBackgroundResource(R.drawable.shape_empty_circle_outline_24dp)
            it.view.textview_list_item_calendar_day.setTextColor(Color.BLACK)
            selectedDay = it
        }
    }

    class JDViewHolder(
        view: View
    ) : ViewHolder(view) {

        private val dayTextView =
            view.textview_list_item_calendar_day //Contains the text of the day (e.g 1,2..)
        private val eventImageView =
            view.imageview_list_item_calendar_event //The event small circle
        var textColor = Color.BLACK
        var backgroundResource = 0

        fun bind(
            day: Day,
            events: List<Events>,
            monthDayOnClickListener: dayOfMonthClickListener,
            onDayClickListener: (JDViewHolder) -> Unit
        ) {

            view.setBackgroundResource(0) //Cleans background resource
            eventImageView.clearColorFilter() //Cleans ImageView resource
            textColor = Color.BLACK //Default text color
            backgroundResource = 0

            if (day.isToday) { //If the day is today update the text color and background
                textColor = Color.WHITE
                backgroundResource = R.drawable.shape_circle_30dp
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
                    lectures.addAll(findLecturesByDay(it.lectures, day))
                    exams.addAll(findExamsByDay(it.exams, day))
                    todos.addAll(findTodosByDay(it.todos, day))
                }
                if (lectures.count() > 0) {
                    paintEventCircle(Lecture.color)
                }
                if (exams.count() > 0) {
                    paintEventCircle(Exam.color)
                }
                if (todos.count() > 0) {
                    paintEventCircle(Todo.color)
                }
            }

            // show all events available for this specific day
            view.setOnClickListener {
                monthDayOnClickListener?.invoke(
                    Events.create(
                        exams,
                        lectures,
                        todos,
                        emptyList()
                    )
                )
                onDayClickListener(this)
            }
        }

        private fun paintEventCircle(color: String) {
            val currColor = eventImageView.colorFilter
            val newColor = Color.parseColor(if (currColor != null) "purple" else color)
            eventImageView.setColorFilter(newColor)
        }

        private fun findLecturesByDay(lectures: List<Lecture>, day: Day): List<Lecture> {
            val weekDay = WeekDay.byNumber(day.value.dayOfWeek)
            return lectures.filter {
                it.weekDay == weekDay && day.isBefore(it.endDate)
            }
        }

        private fun findExamsByDay(exams: List<Exam>, day: Day): List<Exam> =
            exams.filter {
                day.equals(it.startDate)
            }

        private fun findTodosByDay(todos: List<Todo>, day: Day): List<Todo> =
            todos.filter {
                day.equals(it.due)
            }

    }
}


