package org.ionproject.android.calendar

import android.content.Context
import org.ionproject.android.R
import java.util.*

class CalendarWrapper {

    private val calendar = Calendar.getInstance()

    private val today = calendar.time

    val year get() = calendar.get(Calendar.YEAR)
    val month get() = calendar.get(Calendar.MONTH)
    val week get() = calendar.get(Calendar.WEEK_OF_MONTH)
    val day get() = calendar.get(Calendar.DAY_OF_MONTH)
    val dayOfWeek get() = calendar.get(Calendar.DAY_OF_WEEK)
    val isToday get() = calendar.time.compareTo(today) == 0


    fun moveForwardMonth() = calendar.add(Calendar.MONTH, +1)
    fun moveBackwardMonth() = calendar.add(Calendar.MONTH, -1)

    fun moveTo(year: Int = this.year, month: Int = this.month, day: Int = this.day) =
        calendar.set(year, month, day)

    fun moveDays(days: Int) = calendar.add(Calendar.DAY_OF_MONTH, days)

    fun moveForwardDay() = calendar.add(Calendar.DAY_OF_MONTH, +1)

    fun getMonthName(ctx: Context) = Month.values()[month].getName(ctx)

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
}