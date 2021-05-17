package org.ionproject.android.offline.catalogExamSchedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_exam_schedule_item.view.*
import kotlinx.android.synthetic.main.list_item_exams.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ExamDetails

class CatalogExamsListAdapter(
    private val exams: List<ExamDetails>
) : RecyclerView.Adapter<CatalogExamsListAdapter.CatalogExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_exam_schedule_item, parent, false)
        return CatalogExamViewHolder(view)
    }

    override fun getItemCount(): Int = exams.size

    override fun onBindViewHolder(holder: CatalogExamViewHolder, position: Int) {
        holder.bindTo(exams[position])
    }

    class CatalogExamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val examTextView = view.catalog_exam_name_textView
        private val examStartDate = view.catalog_exam_startDate
        private val examEndDate = view.catalog_exam_endDate
        private val examCategory = view.catalog_exam_category
        private val examLocation = view.catalog_exam_location

        fun bindTo(exam: ExamDetails) {
           examTextView.text = exam.name
            examStartDate.text = view.resources.getString(R.string.exam_startDate).format(exam.startDate)
            examEndDate.text = view.resources.getString(R.string.exam_endDate).format(exam.endDate)
            examCategory.text = view.resources.getString(R.string.exam_category).format(exam.category)
            examLocation.text = view.resources.getString(R.string.exam_location).format(exam.location ?: "")
        }
    }
}