package org.ionproject.android.calendar.JDCalendar

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

/**
 * Adapter used by the [GridView] which should contain the days of a month,
 * uses a [CalendarAdapter] to instantiate the views and bind them to its [Day]
 */
class BaseCalendarAdapter<T : CalendarAdapter.ViewHolder>(
    private var calendarAdapter: CalendarAdapter<T>
) : BaseAdapter() {

    /**
     * Represents the current calendar being represented inside
     * in the [Gridview].
     */
    var calendar: Calendar = Calendar.getInstance()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    /** Contains all the instances of [Day], one for each day of the current month represented in the [calendar] */
    private var daysList: List<Day> = calendar.getDaysOfMonth()

    /**
     * Auxiliary method to improve legibility,
     * updates [calendar] with a new one
     * which has been advanced N months
     */
    fun advanceMonths(months: Int) {
        calendar = calendar.monthsFromNow(months)
    }

    /**
     * Updates the adapter an notifies that there has been a
     * data set change.
     */
    fun setAdapter(calendarAdapter: CalendarAdapter<T>) {
        this.calendarAdapter = calendarAdapter
        super.notifyDataSetChanged()
    }

    /**
     * Called when this is being instantiated so that we obtain the
     * days of the current month.
     */
    init {
        daysList = calendar.getDaysOfMonth()
    }

    /**
     * Responsible for instantiating the items within the gridview, its
     * called by the framework.
     *
     * @param position is the position inside the grid at which the method is being called
     * @param convertView is the recycled view used to represent the same item previously
     * @param parent is the view in which this view is contained
     */
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if (parent == null)
            throw IllegalArgumentException("Parent is null")
        var vh: T
        if (convertView != null)
            vh = convertView.tag as T
        else {
            vh = calendarAdapter.onCreateViewHolder(parent)
            vh.view.tag = vh
        }
        calendarAdapter.onBindViewHoler(vh, daysList[position], position)
        return vh.view
    }

    /**
     * Should be called when the calendar instance is updated, so that
     * the gridview updates its child views with the new days from the
     * new calendar instance.
     */
    override fun notifyDataSetChanged() {
        daysList = calendar.getDaysOfMonth()
        super.notifyDataSetChanged()
    }

    /** Returns a [Day] from the [daysList] at the position */
    override fun getItem(position: Int): Day = daysList[position]

    /**
     * Returns the Id of a [Day] from the [daysList] at the position,
     * since the items in this DataSet don't have an ID we utilize the hashCode
     * which is the closest to an ID.
     */
    override fun getItemId(position: Int): Long = daysList[position].hashCode().toLong()

    /** Returns the number of days in the [daysList] */
    override fun getCount(): Int = daysList.size
}

