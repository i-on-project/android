package org.ionproject.android.calendar.JDCalendar

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import java.util.*

private const val WEEKS = 5
private const val WEEK_DAYS = 7
private const val NUMB_OF_DAYS = WEEKS * WEEK_DAYS


class BaseCalendarAdapter<T : CalendarAdapter.ViewHolder>(
    private var calendarAdapter: CalendarAdapter<T>
) : BaseAdapter() {

    var calendar: Calendar = Calendar.getInstance()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun setAdapter(calendarAdapter: CalendarAdapter<T>) {
        this.calendarAdapter = calendarAdapter
        super.notifyDataSetChanged()
    }

    private lateinit var daysList: List<Day>

    init {
        updateDaysOfMonth()
    }

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

    override fun notifyDataSetChanged() {
        updateDaysOfMonth()
        super.notifyDataSetChanged()
    }

    /**
     * Check on which week day the month starts (e.g tuesday,friday)
     * Fill previous month days (e.g 29,30)
     * Fill current month days (e.g 01,02,03,04,05,06...31)
     * Fill next month days (e.g 1,2)
     */
    private fun updateDaysOfMonth() {
        //Set calendar to day 1 of month
        var movedCalendar = calendar.firstDayOfMonth()

        //obtaining current month for later comparison
        val currMonth = movedCalendar.month
        //Get day of week on which the month starts
        val weekDay = movedCalendar.dayOfWeek
        //Number of days varies if the month starts in a sunday or saturday
        val numbOfDays =
            if (weekDay == Calendar.SATURDAY && movedCalendar.lastDayOfMonth == 31 || weekDay == Calendar.SUNDAY)
                NUMB_OF_DAYS + WEEK_DAYS
            else
                NUMB_OF_DAYS

        /*
        If the week day is sunday, its value is 1 because in USA the weeks start in sunday
        therefore we have to add 5 instead of subtracting 2
         */
        movedCalendar = if (weekDay == Calendar.SUNDAY)
            movedCalendar.daysFromNow(-(weekDay + 5))
        else
            movedCalendar.daysFromNow(-(weekDay - 2))

        daysList = (0 until numbOfDays).map {
            val calendar = movedCalendar.daysFromNow(it)
            Day(
                value = calendar.day,
                isDayOfCurrMonth = calendar.month == currMonth,
                isToday = calendar.isToday
            )

        }
    }

    override fun getItem(position: Int): Day = daysList[position]

    override fun getItemId(position: Int): Long = daysList[position].hashCode().toLong()

    override fun getCount(): Int = daysList.size
}

