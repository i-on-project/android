package org.ionproject.android.offline.catalogTimetable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_timetable_event_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.TimetableEvent
import java.util.*

class CatalogTimetableEventListAdapter(
    private val events: List<TimetableEvent>
) : RecyclerView.Adapter<CatalogTimetableEventListAdapter.CatalogEventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_timetable_event_item, parent, false)
        return CatalogEventViewHolder(view)
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: CatalogEventViewHolder, position: Int) {
        holder.bindTo(events[position])
    }

    class CatalogEventViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val eventName = view.catalog_timetable_event_name_textView

        private val beginTime = view.catalog_timetable_event_beginTime

        private val eventDuration = view.catalog_timetable_event_duration

        private val eventWeekday = view.catalog_timetable_event_weekday

        fun bindTo(event: TimetableEvent) {

            if (Locale.getDefault().language == "pt") {
                eventName.text = categoryParser(event.category)
                eventWeekday.text = view.resources.getString(R.string.timetable_weekday)
                    .format(weekdayParser(event.weekday))
            } else {
                eventName.text = event.category
                eventWeekday.text =
                    view.resources.getString(R.string.timetable_weekday).format(event.weekday)
            }

            beginTime.text =
                view.resources.getString(R.string.timetable_beginTime).format(event.beginTime)
            eventDuration.text =
                view.resources.getString(R.string.timetable_duration).format(event.duration)
        }

        /**
         * Parses the category to Portuguese using the docs in
         * https://github.com/i-on-project/integration-data
         */
        private fun categoryParser(category: String): String {
            when (category) {
                "LECTURE" -> return "Aula Teórica"
                "PRACTICE" -> return "Aula Prática"
                "LAB" -> return "Aula de Laboratório"
                "LECTURE-PRACTICE" -> return "Aula Teórico-Prática"
            }

            return category
        }

        /**
         * Parses the weekday to Portuguese using the docs in
         * https://github.com/i-on-project/integration-data
         */
        private fun weekdayParser(weekday: String): String {
            when (weekday) {
                "SU" -> return "DOM"
                "MO" -> return "SEG"
                "TU" -> return "TER"
                "WE" -> return "QUA"
                "TH" -> return "QUI"
                "FR" -> return "SEX"
                "SA" -> return "SAB"
            }

            return weekday
        }
    }
}