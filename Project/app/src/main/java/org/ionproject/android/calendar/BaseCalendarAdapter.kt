package org.ionproject.android.calendar

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*
import kotlin.collections.ArrayList

private const val WEEKS = 5
private const val WEEK_DAYS = 7
private const val NUMB_OF_DAYS = WEEKS * WEEK_DAYS


class BaseCalendarAdapter<T : CalendarAdapter.ViewHolder>(
    private var calendarAdapter: CalendarAdapter<T>,
    private val calendar: CalendarWrapper
) : BaseAdapter() {

    fun setAdapter(calendarAdapter: CalendarAdapter<T>) {
        this.calendarAdapter = calendarAdapter
        super.notifyDataSetChanged()
    }

    /**
     * Contains the viewHolders
     */
    private val viewHolders = ArrayList<T>(NUMB_OF_DAYS)
    private val daysList = mutableListOf<Day>()

    init {
        updateDaysOfMonth()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        if(parent == null)
            throw IllegalArgumentException("Parent is null")
        var vh: T? = viewHolders.elementAtOrNull(position)
        if(vh == null) {
            vh = calendarAdapter.onCreateViewHolder(parent)
            viewHolders.add(vh)
        }
        vh.bind(daysList[position])
        return vh.view
    }

    /**
     * Check on which week day the month starts (e.g tuesday,friday)
     * Fill previous month days (e.g 29,30)
     * Fill current month days (e.g 01,02,03,04,05,06...31)
     * Fill next month days (e.g 1,2)
     */
    private fun updateDaysOfMonth() {
        //Clean days list
        daysList.clear()

        //Set calendar to day 1 of month
        calendar.moveTo(day = 1)

        //obtaining current month for later comparison
        val currMonth = calendar.month
        //Obtaining current year to revert calendar later
        val currYear = calendar.year
        //Get day of week on which the month starts
        val weekDay = calendar.dayOfWeek
        /*
        If the week day is sunday, its value is 1 because in USA the weeks start in sunday
        therefore we have to add 5 instead of subtracting 2
         */
        if (weekDay == Calendar.SUNDAY)
            calendar.moveDays(-(weekDay + 5))
        else
            calendar.moveDays(-(weekDay - 2))

        while (daysList.count() < NUMB_OF_DAYS) {
            val monthOfDay = calendar.month
            daysList.add(Day(calendar.day,monthOfDay == currMonth,calendar.isToday))
            calendar.moveForwardDay()
        }
        /*
        Revert calendar to curr month, day 1 because otherwise when we add or
        subtract a month it may advance an additional month
         */
        calendar.moveTo(currYear,currMonth,1)
    }

    override fun notifyDataSetChanged() {
        updateDaysOfMonth()
        super.notifyDataSetChanged()
    }

    override fun getItem(position: Int): T = viewHolders[position]

    override fun getItemId(position: Int): Long = viewHolders[position].hashCode().toLong()

    override fun getCount(): Int = NUMB_OF_DAYS
}

