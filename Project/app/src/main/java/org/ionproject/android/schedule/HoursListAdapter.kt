package org.ionproject.android.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_schedule_hours_minutes.view.*
import org.ionproject.android.R

typealias Interval = Pair<Moment, Moment>

class HoursListAdapter(startHours: Moment, endHours: Moment) :
    RecyclerView.Adapter<HoursListAdapter.HoursViewHolder>() {

    val intervals: List<Interval> by lazy {
        (startHours until endHours step Moment.ThirtyMinutes).map {
            it to it + Moment.ThirtyMinutes
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_schedule_hours_minutes, parent, false)
        return HoursViewHolder(view)
    }

    override fun getItemCount(): Int = intervals.count()

    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        holder.bind(intervals[position])
    }

    class HoursViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val hours = view.textview_schedule_hours_text

        fun bind(interval: Interval) {
            hours.text = view.resources.getString(
                R.string.label_schedule_hours_placeholder,
                interval.first.toString(),
                interval.second.toString()
            )
        }
    }

}