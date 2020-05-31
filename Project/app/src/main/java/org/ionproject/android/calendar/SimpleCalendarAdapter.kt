package org.ionproject.android.calendar

import android.view.ViewGroup
import org.ionproject.android.calendar.jdcalendar.CalendarAdapter
import org.ionproject.android.calendar.jdcalendar.Day
import org.ionproject.android.calendar.jdcalendar.GridItemView

class SimpleCalendarAdapter : CalendarAdapter<SimpleCalendarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = GridItemView(parent.context)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, day: Day, position: Int) {
        viewHolder.bind(day)
    }

    class ViewHolder(private val gridItemView: GridItemView) :
        CalendarAdapter.ViewHolder(gridItemView) {

        fun bind(day: Day) {
            gridItemView.populate(day)
        }

    }

}