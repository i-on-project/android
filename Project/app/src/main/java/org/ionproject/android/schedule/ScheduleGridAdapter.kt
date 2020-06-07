package org.ionproject.android.schedule

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.grid_item_schedule.view.*
import kotlinx.android.synthetic.main.grid_item_schedule_hours_minutes.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.Lecture

typealias Interval = Pair<Moment, Moment>

class ScheduleGridAdapter(
    startHours: Moment,
    endHours: Moment,
    blockSize: Moment,
    private val model: ScheduleViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val intervals: List<Interval> by lazy {
        (startHours until endHours step blockSize).map {
            it to it + blockSize
        }
    }

    /**
     * Both functions lists are used to distinguish the different ways to perform
     * viewholder creation and binding. This way we avoid having to check if the item
     * is of a certain type. The type correspondes to the list index.
     */
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
            val currColumn = (position + NUMBER_OF_COLUMNS) % NUMBER_OF_COLUMNS
            var lectures = model.lecturesByDayOfWeek[currColumn - 1].toList()
            lectures = lectures.inInterval(intervals[position / NUMBER_OF_COLUMNS])
            (holder as CourseViewHolder).bind(lectures)
        }
    )

    /**
     * Obtain all lectures contained within an interval.
     * For example:
     * All lectures that happen between 8:00 and 8:30
     */
    private fun List<Lecture>.inInterval(interval: Interval) = this.filter {
        if (it.start != null && it.duration != null) {
            val classInterval = Interval(it.start, it.start + it.duration)
            interval.first >= classInterval.first && interval.second <= classInterval.second
        } else {
            false
        }
    }

    /**
     * The type given according the columns where the items are.
     * 1st column are type 0, the rest are type 1
     * This is used to distinguish the viewholders that contain an interval
     * from the viewholders that contain a class
     */
    override fun getItemViewType(position: Int): Int {
        if ((position + NUMBER_OF_COLUMNS) % NUMBER_OF_COLUMNS == 0)
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return createViewHolderFunctions[viewType](parent)
    }

    override fun getItemCount(): Int = intervals.count() * NUMBER_OF_COLUMNS

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bindViewHolderFunctions[holder.itemViewType](holder, position)
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
            view.setBackgroundColor(Color.LTGRAY)
        }
    }

    /**
     * Represents a 30 minute block course
     */
    class CourseViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        val text = view.textview_schedule_text
        val cardView = view.cardview_grid_item_schedule

        fun bind(lectures: List<Lecture>) {
            if (lectures.count() > 0) { // Multiple lectures in the same block
                cardView.isVisible = true
                cardView.setBackgroundColor(Color.GRAY)
                var classesText = lectures[0].summary

                if (lectures.count() > 1) {
                    for (i in 1 until lectures.count()) {
                        classesText += "\n" + lectures[i].summary
                    }
                }
                text.text = classesText
            } else {
                cardView.isVisible = false
                text.text = ""
            }
        }
    }

}