package org.ionproject.android.offline.catalogCalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_calendar_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.Details

class CatalogCalendarDetailsAdapter(
    private val details: List<Details>
) : RecyclerView.Adapter<CatalogCalendarDetailsAdapter.DetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_calendar_item, parent, false)
        return DetailsViewHolder(view)
    }

    override fun getItemCount(): Int = details.size

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        holder.bindTo(details[position])
    }

    class DetailsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.catalog_calendar_item_name
        private val startDateTextView = view.catalog_calendar_item_startDate
        private val endDateTextView = view.catalog_calendar_item_endDate

        fun bindTo(details: Details) {
            nameTextView.text = details.name
            startDateTextView.text = view.resources.getString(R.string.exam_startDate).format(details.startDate)
            endDateTextView.text = view.resources.getString(R.string.exam_endDate).format(details.endDate)
        }
    }
}