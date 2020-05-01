package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import org.ionproject.android.R
import org.ionproject.android.common.Lecture

class LecturesListAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<LecturesListAdapter.LecturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LecturesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_label, parent, false)
        return LecturesViewHolder(view)
    }

    override fun getItemCount(): Int = model.lectures.size

    override fun onBindViewHolder(holder: LecturesViewHolder, position: Int) {
        holder.bindTo(model.lectures[position])
    }

    class LecturesViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val lectureSummary = view.textView_label_example

        fun bindTo(lecture: Lecture) {
            lectureSummary.text = lecture.summary
        }
    }
}