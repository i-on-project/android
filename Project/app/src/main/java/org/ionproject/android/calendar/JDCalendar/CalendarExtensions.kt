package org.ionproject.android.calendar.JDCalendar

import android.content.Context
import org.ionproject.android.R
import java.util.*

/**
 * Contains a set of auxiliary functions and properties to facilitate the use of the Calendar type from Java.
 * Instead of affecting an instance of calendar, they create a new one which has been altered.
 * This approach does decrease the performance of the code if in single-threaded environment, but
 * facilitates concurrent access if in a multi-threaded environment.
 * It also provides much better legibility.
 */

/** Returns the current year at which this instance is at */
val Calendar.year get() = get(Calendar.YEAR)
/** Returns the current month at which this instance is at */
val Calendar.month get() = get(Calendar.MONTH)
/** Returns the current week at which this instance is at */
val Calendar.week get() = get(Calendar.WEEK_OF_MONTH)
/** Returns the current day of month at which this instance is at */
val Calendar.day get() = get(Calendar.DAY_OF_MONTH)
/** Returns the current day of week at which this instance is at */
val Calendar.dayOfWeek get() = get(Calendar.DAY_OF_WEEK)
/** Returns true if the current day at which this instance is at is today*/
val Calendar.isToday: Boolean
    get() {
        val today = Calendar.getInstance()
        return day == today.day && month == today.month && year == today.year
    }

/**
 * Returns a new calendar instance which
 * has been advanced N days from the day
 * of the instance it was called from
 */
fun Calendar.daysFromNow(days: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return calendar
}

/**
 * Returns a new calendar instance which
 * has been advanced N months from the month
 * of the instance it was called from
 */
fun Calendar.monthsFromNow(months: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.add(Calendar.MONTH, months)
    return calendar
}

/**
 * Returns a new calendar instance which
 * has been set to the first day of the
 * month
 */
fun Calendar.firstDayOfMonth(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar
}

/**
 * Returns last day of the month at which this instance is at
 */
val Calendar.lastDayOfMonth: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.day
    }

/** Return the name of the month at which this instance is at */
fun Calendar.getMonthName(ctx: Context) = Month.values()[month].getName(ctx)

/**
 * Represents a Month and associated with each month is the resource from Strings.xml
 * this way, when the application language is altered, so is the month
 */
private enum class Month(private val monthResId: Int) {
    JANUARY(R.string.january),
    FEBRUARY(R.string.february),
    MARCH(R.string.march),
    APRIL(R.string.april),
    MAY(R.string.may),
    JUNE(R.string.june),
    JULY(R.string.july),
    AUGUST(R.string.august),
    OCTOBER(R.string.october),
    SEPTEMBER(R.string.september),
    NOVEMBER(R.string.november),
    DECEMBER(R.string.december);

    fun getName(ctx: Context) = ctx.resources.getString(monthResId)
}
