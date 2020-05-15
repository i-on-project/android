package org.ionproject.android.calendar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_event_item.view.*
import org.ionproject.android.R
import org.ionproject.android.TAG
import org.ionproject.android.common.model.Event

class EventsListAdapter(
    private val viewModel: EventsListViewModel
) : RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_event_item, parent, false)

        return EventsListViewHolder(view)
    }

    override fun getItemCount(): Int = viewModel.events.size

    override fun onBindViewHolder(holder: EventsListViewHolder, position: Int) {
        holder.bindTo(viewModel.events[position])
    }

    class EventsListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventType = view.textView_list_event_type
        private val eventSummary = view.textView_list_event_summary
        private val eventDescription = view.textView_list_event_description

        fun bindTo(event: Event) {
            eventType.text = event.eventType
            eventSummary.text = event.summary
            eventDescription.text = event.description
        }
    }
}