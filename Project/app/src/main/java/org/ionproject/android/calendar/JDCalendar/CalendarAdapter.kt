package org.ionproject.android.calendar.JDCalendar

import android.view.View
import android.view.ViewGroup

/**
 * Public CalendarAdapter which can be extended from to create a custom adapter for the calendar.
 * This adapter is used to instantiate the views that represent the days.
 */
abstract class CalendarAdapter<E : CalendarAdapter.ViewHolder> {

    /**
     * Should inflate a view from the [parent] and return a [CalendarAdapter.ViewHolder] which
    contains that view.
     */
    abstract fun onCreateViewHolder(parent: ViewGroup): E

    /**
     * Responsible for binding the view to its respective day of the month.
     * It should contain the view elements(e.g TextView..) as properties because the ViewHolders
     * will be saved and that way findViewById is only called once for each element.
     */
    abstract class ViewHolder(private val view: View) {

        /**
         * Should bind the [day] to the view passed in the primary constructor
         */
        abstract fun bind(day: Day)
    }
}