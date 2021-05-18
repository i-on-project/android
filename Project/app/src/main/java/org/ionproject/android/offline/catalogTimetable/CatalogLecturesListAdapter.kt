package org.ionproject.android.offline.catalogTimetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_timetable_item.view.*
import kotlinx.android.synthetic.main.fragment_catalog_timetable.*
import kotlinx.android.synthetic.main.list_item_lectures.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ClassesDetails
import org.ionproject.android.offline.models.TimetableEvent
import java.util.*

class CatalogLecturesListAdapter(
    private val lectures: List<ClassesDetails>,
    private val context: Context
) : RecyclerView.Adapter<CatalogLecturesListAdapter.CatalogLectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_timetable_item, parent, false)
        return CatalogLectureViewHolder(view,context)
    }

    override fun getItemCount(): Int = lectures.size

    override fun onBindViewHolder(holder: CatalogLectureViewHolder, position: Int) {
        holder.bindTo(lectures[position])
    }

    class CatalogLectureViewHolder(val view: View, val context: Context) : RecyclerView.ViewHolder(view) {

        private val classname = view.catalog_timetable_class_name_textView

        private val sectionRecyclerView = view.catalog_timetable_section_recyclerView

        fun bindTo(lecture: ClassesDetails) {
            classname.text = lecture.acr.toUpperCase(Locale.ROOT)

            sectionRecyclerView.adapter =
                lecture.sections?.let { CatalogTimetableSectionListAdapter(it,context) }
            sectionRecyclerView.layoutManager = LinearLayoutManager(context)

            classname.setOnClickListener {
                if(sectionRecyclerView.isVisible)
                    sectionRecyclerView.visibility = View.GONE
                else
                    sectionRecyclerView.visibility = View.VISIBLE
            }

        }
    }
}