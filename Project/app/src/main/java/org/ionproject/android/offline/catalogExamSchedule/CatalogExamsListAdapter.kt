package org.ionproject.android.offline.catalogExamSchedule

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_exam_schedule_item.view.*
import kotlinx.android.synthetic.main.list_item_exams.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ExamDetails
import java.util.*
import kotlin.collections.ArrayList

class CatalogExamsListAdapter(
    examsParameter: MutableList<ExamDetails>
) : RecyclerView.Adapter<CatalogExamsListAdapter.CatalogExamViewHolder>(), Filterable{

    private val examsInside = examsParameter
    private val examsFull = ArrayList(examsParameter)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_exam_schedule_item, parent, false)

        return CatalogExamViewHolder(view)
    }

    override fun getItemCount(): Int = examsInside.size

    override fun onBindViewHolder(holder: CatalogExamViewHolder, position: Int) {
        holder.bindTo(examsInside[position])
    }

    override fun getFilter(): Filter {
        return examFilter
    }

    private val examFilter: Filter = object : Filter() {

        override fun performFiltering(constraint: CharSequence): FilterResults {

            Log.d("Catalog", "filtering with query [$constraint] and examinside size :${examsInside.size} exam full size: ${examsFull.size}")

            val filteredList: MutableList<ExamDetails> = mutableListOf()

            if (constraint.isEmpty()) {
                filteredList.addAll(examsFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in examsFull) {
                    if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            examsInside.clear()
            examsInside.addAll(results.values as MutableList<ExamDetails>)
            notifyDataSetChanged()
        }
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