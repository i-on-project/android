package org.ionproject.android.calendar.JDCalendar

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

private const val WEEK_DAYS = 7

/**
 * Adapter used by the [GridView] which should contain the days of a month,
 * uses a [CalendarAdapter] to instantiate the views and bind them to its [Day]
 */
class BaseCalendarAdapter<T : CalendarAdapter.ViewHolder>(
    private var calendarAdapter: CalendarAdapter<T>
) : BaseAdapter() {

    /** Contains all the instances of [Day], one for each day of the current month represented in the [calendar] */
    private lateinit var daysList: List<Day>

    /**
     * Represents the current calendar being represented inside
     * in the [Gridview].
     */
    var calendar: Calendar = Calendar.getInstance()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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
        updateDaysOfMonth()
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
        vh.bind(daysList[position])
        return vh.view
    }

    /**
     * Should be called when the calendar instance is updated, so that
     * the gridview updates its child views with the new days from the
     * new calendar instance.
     */
    override fun notifyDataSetChanged() {
        updateDaysOfMonth()
        super.notifyDataSetChanged()
    }

    /**
     * Updates [daysList] with a new list containing the days of the current
     * month represented in the calendar
     *
     * Logic:
     * Check on which week day the month starts (e.g tuesday,friday)
     * Obtain previous month days (e.g 29,30)
     * Obtain current month days (e.g 01,02,03,04,05,06...31)
     * Obtain next month days (e.g 1,2)
     */
    private fun updateDaysOfMonth() {
        //Set calendar to day 1 of month
        var movedCalendar = calendar.firstDayOfMonth()

        //obtaining current month for later comparison
        val currMonth = movedCalendar.month
        //Get day of week on which the month starts
        val weekDay = movedCalendar.dayOfWeek

        //Number of days presented in the calendar varies if the month starts in a sunday or saturday
        val numbOfDays =
            if (weekDay == Calendar.SATURDAY && movedCalendar.lastDayOfMonth == 31 || weekDay == Calendar.SUNDAY)
                6 * WEEK_DAYS
            else
                5 * WEEK_DAYS
        /*
        If the week day is sunday, its value is 1 because in USA the weeks start in sunday
        therefore we have to add 5 instead of subtracting 2
         */
        movedCalendar = if (weekDay == Calendar.SUNDAY)
            movedCalendar.daysFromNow(-(weekDay + 5))
        else
            movedCalendar.daysFromNow(-(weekDay - 2))

        /*
        Updating the days list
         */
        daysList = (0 until numbOfDays).map {
            val calendar = movedCalendar.daysFromNow(it)
            Day(
                value = calendar.day,
                isDayOfCurrMonth = calendar.month == currMonth,
                isToday = calendar.isToday
            )

        }
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

