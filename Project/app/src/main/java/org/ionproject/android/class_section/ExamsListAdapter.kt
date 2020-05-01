package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import org.ionproject.android.R
import org.ionproject.android.common.ExamSummary

class ExamsListAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<ExamsListAdapter.ExamsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_label, parent, false)
        return ExamsViewHolder(view)
    }

    override fun getItemCount(): Int = model.exams.size

    override fun onBindViewHolder(holder: ExamsViewHolder, position: Int) {
        holder.bindTo(model.exams[position])
    }

    class ExamsViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val examName = view.textView_label_example

        fun bindTo(examSummary: ExamSummary) {
            examName.text = examSummary.summary
        }
    }
}