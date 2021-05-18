package org.ionproject.android.offline.catalogTimetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_timetable_section_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.Section

class CatalogTimetableSectionListAdapter(
    private val sections: List<Section>,
    private val context: Context
) : RecyclerView.Adapter<CatalogTimetableSectionListAdapter.CatalogSectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogSectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_timetable_section_item, parent, false)
        return CatalogSectionViewHolder(view,context)
    }

    override fun getItemCount(): Int = sections.size

    override fun onBindViewHolder(holder: CatalogSectionViewHolder, position: Int) {
        holder.bindTo(sections[position])
    }

    class CatalogSectionViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

        val sectionName = view.catalog_timetable_section_name_textView


        val eventRecyclerView = view.catalog_timetable_event_recyclerView

        fun bindTo(section: Section) {
            sectionName.text = section.section

            eventRecyclerView.adapter = CatalogTimetableEventListAdapter(section.events)
            eventRecyclerView.layoutManager =  LinearLayoutManager(context)

            sectionName.setOnClickListener {
                if(eventRecyclerView.isVisible)
                    eventRecyclerView.visibility = View.GONE
                else
                    eventRecyclerView.visibility = View.VISIBLE
            }
        }
    }
}