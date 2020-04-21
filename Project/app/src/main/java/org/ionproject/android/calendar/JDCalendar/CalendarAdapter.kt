package org.ionproject.android.calendar.JDCalendar

import android.view.View
import android.view.ViewGroup


abstract class CalendarAdapter<E : CalendarAdapter.ViewHolder> {

    abstract fun onCreateViewHolder(parent: ViewGroup): E

    abstract class ViewHolder(val view: View) {

        abstract fun bind(day: Day)
    }
}