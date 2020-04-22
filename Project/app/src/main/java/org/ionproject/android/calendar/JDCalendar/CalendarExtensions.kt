package org.ionproject.android.calendar.JDCalendar

import android.content.Context
import org.ionproject.android.R
import java.util.*

fun Calendar.daysFromNow(days: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.add(Calendar.DAY_OF_MONTH, days)
    return calendar
}

fun Calendar.monthsFromNow(months: Int): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.add(Calendar.MONTH, months)
    return calendar
}

fun Calendar.firstDayOfMonth(): Calendar {
    val calendar = Calendar.getInstance()
    calendar.time = this.time
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    return calendar
}


val Calendar.lastDayOfMonth: Int
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = this.time
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        return calendar.day
    }
val Calendar.year get() = get(Calendar.YEAR)
val Calendar.month get() = get(Calendar.MONTH)
val Calendar.week get() = get(Calendar.WEEK_OF_MONTH)
val Calendar.day get() = get(Calendar.DAY_OF_MONTH)
val Calendar.dayOfWeek get() = get(Calendar.DAY_OF_WEEK)
val Calendar.isToday get() = time.compareTo(Calendar.getInstance().time) == 0

fun Calendar.getMonthName(ctx: Context) = Month.values()[month].getName(ctx)

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