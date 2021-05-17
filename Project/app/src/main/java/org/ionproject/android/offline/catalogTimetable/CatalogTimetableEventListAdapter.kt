package org.ionproject.android.offline.catalogTimetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_timetable_event_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.TimetableEvent

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
            eventName.text = event.category
            beginTime.text = view.resources.getString(R.string.timetable_beginTime).format(event.beginTime)
            eventDuration.text = view.resources.getString(R.string.timetable_duration).format(event.duration)
            eventWeekday.text = view.resources.getString(R.string.timetable_weekday).format(event.weekday)
        }
    }
}