package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_exams.view.*
import org.ionproject.android.R
import org.ionproject.android.calendar.export
import org.ionproject.android.common.model.Exam
import org.ionproject.jdcalendar.day
import org.ionproject.jdcalendar.month
import org.ionproject.jdcalendar.year

class ExamsListAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<ExamsListAdapter.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_exams, parent, false)
        return ExamViewHolder(view)
    }

    override fun getItemCount(): Int = model.events.exams.size

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        holder.bindTo(model.events.exams[position])
    }

    class ExamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val examTextView = view.textview_list_item_exams
        private val exportButton = view.button_list_item_exams_export

        fun bindTo(exam: Exam) {
            examTextView.text = view.resources.getString(
                R.string.placeholder_exam_all,
                exam.summary,
                exam.endDate.day,
                exam.endDate.month,
                exam.endDate.year
            )
            exportButton.setOnClickListener {
                exam.export(it.context)
            }
        }
    }
}