package org.ionproject.android.schedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grid_item_schedule.view.*
import kotlinx.android.synthetic.main.grid_item_schedule_hours_minutes.view.*
import org.ionproject.android.R

typealias Interval = Pair<Moment, Moment>

private const val NUMBER_OF_COLUMNS = 8

class ScheduleGridAdapter(startHours: Moment, endHours: Moment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val intervals: List<Interval> by lazy {
        (startHours until endHours step Moment.ThirtyMinutes).map {
            it to it + Moment.ThirtyMinutes
        }
    }

    private val createViewHolderFunctions = listOf(
        { parent: ViewGroup ->
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item_schedule_hours_minutes, parent, false)
            HoursViewHolder(view)
        },
        { parent: ViewGroup ->
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.grid_item_schedule, parent, false)
            CourseViewHolder(view)
        }
    )

    private val bindViewHolderFunctions = listOf(
        { holder: RecyclerView.ViewHolder, position: Int ->
            (holder as HoursViewHolder).bind(intervals[position / NUMBER_OF_COLUMNS])
        },
        { holder: RecyclerView.ViewHolder, position: Int ->
            (holder as CourseViewHolder).bind()
        }
    )

    /**
     * Each type corresponds to an index in the viewHolderFunctions
     */
    override fun getItemViewType(position: Int): Int {
        if (position == 0 || position % NUMBER_OF_COLUMNS == 0)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolderFunctions[viewType].invoke(parent)
    }

    override fun getItemCount(): Int = intervals.count() * NUMBER_OF_COLUMNS

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolderFunctions[holder.itemViewType].invoke(holder, position)
    }

    /**
     * Represents the left most items in the grid, each item is a 30 minute period
     */
    class HoursViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val hours = view.textview_schedule_hours

        fun bind(interval: Interval) {
            hours.text = view.resources.getString(
                R.string.label_schedule_hours_placeholder,
                interval.first.toString(),
                interval.second.toString()
            )
        }
    }

    /**
     * Represents a 30 minute block course
     */
    class CourseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind() {
            view.textview_schedule_text.text = "PG"
        }
    }

}