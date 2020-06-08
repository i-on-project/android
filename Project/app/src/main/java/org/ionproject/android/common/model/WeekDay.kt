package org.ionproject.android.common.model

import android.content.Context
import org.ionproject.android.R

/**
 * Represents a weekDay and associated with each month is the resource from Strings.xml
 * this way, when the application language is altered, so is the month
 */
enum class WeekDay(private val weekDayResId: Int) {
    MONDAY(R.string.monday),
    TUESDAY(R.string.tuesday),
    WEDNESDAY(R.string.wednesday),
    THURSDAY(R.string.thursday),
    FRIDAY(R.string.friday),
    SATURDAY(R.string.saturday),
    SUNDAY(R.string.sunday);

    fun getName(ctx: Context) = ctx.resources.getString(weekDayResId)

    companion object {
        fun getByShortName(shortName: String): WeekDay {
            return when (shortName) {
                "MO" -> MONDAY
                "TU" -> TUESDAY
                "WE" -> WEDNESDAY
                "TH" -> THURSDAY
                "FR" -> FRIDAY
                "SA" -> SATURDAY
                "SU" -> SUNDAY
                else -> throw IllegalArgumentException("$shortName is not a valid shot name")
            }
        }
    }
}