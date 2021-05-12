package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_lectures.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.TimetableEvent

class CatalogLecturesListAdapter(
    private val lectures: List<TimetableEvent>
) : RecyclerView.Adapter<CatalogLecturesListAdapter.CatalogLectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogLectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_lectures, parent, false)
        return CatalogLectureViewHolder(view)
    }

    override fun getItemCount(): Int = lectures.size

    override fun onBindViewHolder(holder: CatalogLectureViewHolder, position: Int) {
        holder.bindTo(lectures[position])
    }

    class CatalogLectureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val lectureTextView = view.textview_list_item_lectures
        private val locationTextView = view.textview_list_item_lectures_location

        fun bindTo(lecture: TimetableEvent) {

            lectureTextView.text = view.resources.getString(
                R.string.placeholder_lecture_all,
                lecture.category,
                lecture.weekday,
                lecture.beginTime,
                "",
                lecture.duration,
                ""
            )

            locationTextView.text = lecture.location?.get(0) ?: ""
        }
    }
}