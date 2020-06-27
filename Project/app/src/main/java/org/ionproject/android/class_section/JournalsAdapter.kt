package org.ionproject.android.class_section

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_label.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.Journal

class JournalsAdapter(
    private val model: ClassSectionViewModel
) : RecyclerView.Adapter<JournalsAdapter.JournalViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_label, parent, false)
        return JournalViewHolder(view)
    }

    override fun getItemCount(): Int = model.events.journals.size

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.bindTo(model.events.journals[position])
    }

    class JournalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val journalSummary = view.textView_label_example

        fun bindTo(journalInfo: Journal) {
            journalSummary.text = journalInfo.summary
        }
    }
}