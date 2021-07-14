package org.ionproject.android.offline.catalogTimetable

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_timetable_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ClassesDetails
import java.util.*

class CatalogLecturesListAdapter(
    private val lectures: MutableList<ClassesDetails>,
    private val context: Context
) : RecyclerView.Adapter<CatalogLecturesListAdapter.CatalogLectureViewHolder>(), Filterable {

    private val lecturesInside = lectures
    private val lecturesFull = ArrayList(lectures)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_timetable_item, parent, false)
        return CatalogLectureViewHolder(view, context)
    }

    override fun getItemCount(): Int = lecturesInside.size

    override fun onBindViewHolder(holder: CatalogLectureViewHolder, position: Int) {
        holder.bindTo(lecturesInside[position])
    }

    override fun getFilter(): Filter {
        return lecturesFilter
    }

    private val lecturesFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {

            val filteredList: MutableList<ClassesDetails> = mutableListOf()

            if (constraint.isEmpty()) {
                filteredList.addAll(lecturesFull)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in lecturesFull) {
                    if (item.acr.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            lecturesInside.clear()
            lecturesInside.addAll(results.values as MutableList<ClassesDetails>)
            notifyDataSetChanged()
        }
    }

    class CatalogLectureViewHolder(val view: View, val context: Context) :
        RecyclerView.ViewHolder(view) {

        private val classname = view.catalog_timetable_class_name_textView

        private val sectionRecyclerView = view.catalog_timetable_section_recyclerView

        fun bindTo(lecture: ClassesDetails) {
            classname.text = lecture.acr.toUpperCase(Locale.ROOT)

            sectionRecyclerView.adapter =
                lecture.sections?.let { CatalogTimetableSectionListAdapter(it, context) }
            sectionRecyclerView.layoutManager = LinearLayoutManager(context)

            classname.setOnClickListener {
                if (sectionRecyclerView.isVisible)
                    sectionRecyclerView.visibility = View.GONE
                else
                    sectionRecyclerView.visibility = View.VISIBLE
            }

        }
    }
}