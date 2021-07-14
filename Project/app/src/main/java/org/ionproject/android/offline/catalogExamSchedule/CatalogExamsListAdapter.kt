package org.ionproject.android.offline.catalogExamSchedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_exam_schedule_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.ExamDetails
import java.util.*
import kotlin.collections.ArrayList

class CatalogExamsListAdapter(
    examsParameter: MutableList<ExamDetails>
) : RecyclerView.Adapter<CatalogExamsListAdapter.CatalogExamViewHolder>(), Filterable {

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

            val filteredList: MutableList<ExamDetails> = mutableListOf()

            if (constraint.isEmpty()) {
                filteredList.addAll(examsFull)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
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
        private val examDayTextView = view.catalog_exam_day
        private val examStartDate = view.catalog_exam_startDate
        private val examEndDate = view.catalog_exam_endDate
        private val examCategory = view.catalog_exam_category
        private val examLocation = view.catalog_exam_location

        fun bindTo(exam: ExamDetails) {

            val examDay = dateParser(exam.startDate)[0]

            examTextView.text = exam.name

            examDayTextView.text =
                view.resources.getString(R.string.exam_day).format(examDay)

            examStartDate.text =
                view.resources.getString(R.string.exam_startDate)
                    .format(dateParser(exam.startDate)[1])

            examEndDate.text =
                view.resources.getString(R.string.exam_endDate).format(dateParser(exam.endDate)[1])

            if (Locale.getDefault().language == "pt")
                examCategory.text =
                    view.resources.getString(R.string.exam_category)
                        .format(categoryParser(exam.category))
            else
                examCategory.text =
                    view.resources.getString(R.string.exam_category).format(exam.category)

            examLocation.text = view.resources.getString(R.string.exam_location)
                .format(exam.location ?: view.resources.getString(R.string.exam_location_tbd))
        }

        /**
         * parses the date from Coordinated Universal Time (UTC) - ISO 8601
         * to a more readable format
         */
        private fun dateParser(date: String): List<String> {
            val split = date.split("T")
            val day = split[0]
            val hour = split[1].substring(0, split[1].length - 7); //removes trailing zeroes

            return listOf(day, hour)
        }

        /**
         * Parses the category to Portuguese using the docs in
         * https://github.com/i-on-project/integration-data
         */
        private fun categoryParser(category: String): String {
            when (category) {
                "TEST" -> return "Teste"
                "EXAM_NORMAL" -> return "Exame época normal"
                "EXAM_ALTERN" -> return "Exame época de recurso"
                "EXAM_SPECIAL" -> return "Exame época especial"
            }

            return category
        }
    }
}