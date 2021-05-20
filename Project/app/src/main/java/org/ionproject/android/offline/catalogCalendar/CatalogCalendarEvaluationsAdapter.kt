package org.ionproject.android.offline.catalogCalendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_calendar_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.models.Evaluation

class CatalogCalendarEvaluationsAdapter(
    private val evaluations: List<Evaluation>
) : RecyclerView.Adapter<CatalogCalendarEvaluationsAdapter.EvaluationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.catalog_calendar_item, parent, false)
        return EvaluationViewHolder(view)
    }

    override fun getItemCount(): Int = evaluations.size

    override fun onBindViewHolder(holder: EvaluationViewHolder, position: Int) {
        holder.bindTo(evaluations[position])
    }

    class EvaluationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        private val nameTextView = view.catalog_calendar_item_name
        private val startDateTextView = view.catalog_calendar_item_startDate
        private val endDateTextView = view.catalog_calendar_item_endDate

        fun bindTo(evaluation: Evaluation) {
            nameTextView.text = evaluation.name
            startDateTextView.text =
                view.resources.getString(R.string.exam_startDate).format(evaluation.startDate)
            endDateTextView.text =
                view.resources.getString(R.string.exam_endDate).format(evaluation.endDate)
        }
    }
}