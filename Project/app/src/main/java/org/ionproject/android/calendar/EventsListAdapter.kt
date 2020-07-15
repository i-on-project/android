package org.ionproject.android.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_event_item.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.jdcalendar.*
import org.ionproject.android.common.model.*

class EventsListAdapter(
    private val viewModel: EventsListViewModel
) : RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_event_item, parent, false)

        return EventsListViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.events.size

    override fun onBindViewHolder(holder: EventsListViewHolder, position: Int) {
        holder.bindTo(viewModel.events[position])
    }

    class EventsListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val eventType = view.textView_list_event_type
        private val eventSummary = view.textView_list_event_summary
        private val eventExtraLabel1 = view.textView_list_event_extra_label1
        private val eventExtraLabel2 = view.textView_list_event_extra_label2
        private val eventExtraResult1 = view.textView_list_event_extra_result1
        private val eventExtraResult2 = view.textView_list_event_extra_result2
        private val eventColor = view.imageview_list_item_event_color
        private val eventExportButton = view.button_list_item_event_export

        fun bindTo(event: Event) {
            eventType.text = event.eventType
            eventSummary.text = event.summary

            clearTextFromExtras()

            when (event) {
                is Lecture -> showInfoAboutALecture(event)
                is Exam -> showInfoAboutAnExam(event)
                is Todo -> showInfoAboutATodo(event)
            }
        }

        /**
         * This method should show which week day the [Lecture] starts and his duration
         * e.g: Week day: Monday  Duration: 11:30h - 13:00h
         */
        private fun showInfoAboutALecture(lecture: Lecture) {
            eventColor.setColorFilter(Color.parseColor(Lecture.color))
            eventExtraLabel1.text = view.resources.getText(R.string.label_list_event_duration)

            val startMoment = Moment.fromCalendar(lecture.start)
            val endMoment = startMoment + lecture.duration

            eventExtraResult1.text = view.resources.getString(
                R.string.placeholder_interval,
                startMoment.hours.fillWithZero(),
                startMoment.minutes.fillWithZero(),
                endMoment.hours.fillWithZero(),
                endMoment.minutes.fillWithZero()
            )
            eventExportButton.setOnClickListener {
                lecture.export(it.context)
            }
            if (lecture.location != null) {
                eventExtraLabel2.text = view.resources.getText(R.string.label_list_event_location)
                eventExtraResult2.text = lecture.location
            }
        }

        /**
         * This method should show which week day the [Exam] start and his end time
         * e.g: Starts: 22/06/2020 - 14:00h  Ends: 22/06/2020 - 17:00h
         */
        private fun showInfoAboutAnExam(exam: Exam) {
            eventColor.setColorFilter(Color.parseColor(Exam.color))
            eventExtraLabel1.text = view.resources.getText(R.string.label_list_event_starts)

            var day = exam.startDate.day.fillWithZero()
            var month = (exam.startDate.month).fillWithZero()
            var year = exam.startDate.year.toString()

            var hour = exam.startDate.hour.fillWithZero()
            var minute = exam.startDate.minute.fillWithZero()

            eventExtraResult1.text = view.resources.getString(
                R.string.placeholder_time,
                day,
                month,
                year,
                hour,
                minute
            )
            eventExtraLabel2.text = view.resources.getText(R.string.label_list_event_ends)

            day = exam.endDate.day.fillWithZero()
            month = (exam.endDate.month).fillWithZero()
            year = exam.endDate.year.toString()

            hour = exam.endDate.hour.fillWithZero()
            minute = exam.endDate.minute.fillWithZero()

            eventExtraResult2.text = view.resources.getString(
                R.string.placeholder_time,
                day,
                month,
                year,
                hour,
                minute
            )
            eventExportButton.setOnClickListener {
                exam.export(it.context)
            }
        }

        /**
         * This method should show which week day the [Todo]'s delivery date
         * e.g: Delivery: 15/06/2020 - 23:59h
         */
        private fun showInfoAboutATodo(todo: Todo) {
            eventColor.setColorFilter(Color.parseColor(Todo.color))
            eventExtraLabel1.text = view.resources.getText(R.string.label_list_event_delivery)

            val day = todo.due.day.fillWithZero()
            val month = todo.due.month.fillWithZero()
            val year = todo.due.year.toString()

            val hour = todo.due.hour.fillWithZero()
            val minute = todo.due.minute.fillWithZero()

            eventExtraResult1.text = view.resources.getString(
                R.string.placeholder_time,
                day,
                month,
                year,
                hour,
                minute
            )
            eventExportButton.setOnClickListener {
                todo.export(it.context)
            }
        }

        private fun clearTextFromExtras() {
            eventExtraLabel1.text = ""
            eventExtraResult1.text = ""
            eventExtraLabel2.text = ""
            eventExtraResult2.text = ""
        }
    }
}
