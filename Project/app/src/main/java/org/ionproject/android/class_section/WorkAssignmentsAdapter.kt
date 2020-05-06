package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import org.ionproject.android.R
import org.ionproject.android.common.TodoSummary

class WorkAssignmentsAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<WorkAssignmentsAdapter.WorkAssignmentsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkAssignmentsHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_label, parent, false)
        return WorkAssignmentsHolder(view)
    }

    override fun getItemCount(): Int = model.workAssignments.size

    override fun onBindViewHolder(holder: WorkAssignmentsHolder, position: Int) {
        holder.bindTo(model.workAssignments[position])
    }

    class WorkAssignmentsHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val workAssignmentSummary = view.textView_label_example

        fun bindTo(todo: TodoSummary) {
            workAssignmentSummary.text = todo.summary
        }
    }
}