package org.ionproject.android.offline

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_exams.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ExamDetails

class CatalogExamsListAdapter(
    private val exams: List<ExamDetails>
) : RecyclerView.Adapter<CatalogExamsListAdapter.CatalogExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_exams, parent, false)
        return CatalogExamViewHolder(view)
    }

    override fun getItemCount(): Int = exams.size

    override fun onBindViewHolder(holder: CatalogExamViewHolder, position: Int) {
        holder.bindTo(exams[position])
    }

    class CatalogExamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val examTextView = view.textview_list_item_exams

        fun bindTo(exam: ExamDetails) {
            examTextView.text = view.resources.getString(
                R.string.placeholder_lecture_all,
                exam.category,
                "",
                exam.startDate,
                "",
                exam.endDate,
                ""
            )
        }
    }
}