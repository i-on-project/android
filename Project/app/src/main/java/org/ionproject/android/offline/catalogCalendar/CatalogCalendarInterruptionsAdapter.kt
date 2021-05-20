package org.ionproject.android.offline.catalogCalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_calendar_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.CalendarEvent

class CatalogCalendarInterruptionsAdapter(
    private val interruptions: List<CalendarEvent>
) : RecyclerView.Adapter<CatalogCalendarInterruptionsAdapter.InterruptionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterruptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_calendar_item, parent, false)
        return InterruptionViewHolder(view)
    }

    override fun getItemCount(): Int = interruptions.size

    override fun onBindViewHolder(holder: InterruptionViewHolder, position: Int) {
        holder.bindTo(interruptions[position])
    }

    class InterruptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.catalog_calendar_item_name
        private val startDateTextView = view.catalog_calendar_item_startDate
        private val endDateTextView = view.catalog_calendar_item_endDate

        fun bindTo(calendarEvent: CalendarEvent) {
            nameTextView.text = calendarEvent.name
            startDateTextView.text =
                view.resources.getString(R.string.exam_startDate).format(calendarEvent.startDate)
            endDateTextView.text =
                view.resources.getString(R.string.exam_endDate).format(calendarEvent.endDate)
        }
    }
}