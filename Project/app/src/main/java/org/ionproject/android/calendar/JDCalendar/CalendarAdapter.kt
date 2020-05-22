package org.ionproject.android.calendar.jdcalendar

import android.view.View
import android.view.ViewGroup

/**
 * Public CalendarAdapter which can be extended from to create a custom adapter for the calendar.
 * This adapter is used to instantiate the views that represent the days.
 */
abstract class CalendarAdapter<VH : CalendarAdapter.ViewHolder> {

    /**
     * Should inflate a view from the [parent] and return a [CalendarAdapter.ViewHolder] which
    contains that view.
     */
    abstract fun onCreateViewHolder(parent: ViewGroup): VH


    /**
     * Should bind a [ViewHolder] to the content of the view
     */
    abstract fun onBindViewHolder(viewHolder: VH, day: Day, position: Int)

    /**
     * Responsible for binding the view to its respective day of the month.
     * It should contain the view elements(e.g TextView..) as properties because the ViewHolders
     * will be saved and that way findViewById is only called once for each element.
     */
    abstract class ViewHolder(val view: View)

}