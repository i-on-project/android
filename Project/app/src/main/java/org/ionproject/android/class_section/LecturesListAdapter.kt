package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_lectures.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.export
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.Moment
import org.ionproject.android.common.model.fillWithZero

class LecturesListAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<LecturesListAdapter.LectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_lectures, parent, false)
        return LectureViewHolder(view)
    }

    override fun getItemCount(): Int = model.events.lectures.size

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bindTo(model.events.lectures[position])
    }

    class LectureViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val lectureTextView = view.textview_list_item_lectures
        private val exportButton = view.button_list_item_lectures_export

        fun bindTo(lecture: Lecture) {
            val startMoment = Moment.fromCalendar(lecture.start)
            val endMoment = startMoment + lecture.duration

            lectureTextView.text = view.resources.getString(
                R.string.placeholder_lecture,
                lecture.summary,
                lecture.weekDay.getName(view.context),
                startMoment.hours.fillWithZero(),
                startMoment.minutes.fillWithZero(),
                endMoment.hours.fillWithZero(),
                endMoment.minutes.fillWithZero()
            )

            exportButton.setOnClickListener {
                lecture.export(it.context)
            }
        }
    }
}