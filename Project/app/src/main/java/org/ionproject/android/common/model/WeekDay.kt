package org.ionproject.android.common.model

import android.content.Context
import org.ionproject.android.R

/**
 * Represents a weekDay and associated with each month is the resource from Strings.xml
 * this way, when the application language is altered, so is the month
 */
enum class WeekDay(private val weekDayResId: Int) {
    MONDAY(R.string.label_monday_schedule),
    TUESDAY(R.string.label_tuesday_schedule),
    WEDNESDAY(R.string.label_wednesday_schedule),
    THURSDAY(R.string.label_thursday_schedule),
    FRIDAY(R.string.label_friday_schedule),
    SATURDAY(R.string.label_saturday_schedule),
    SUNDAY(R.string.label_sunday_schedule);

    fun getName(ctx: Context) = ctx.resources.getString(weekDayResId)

    companion object {
        fun byShortName(shortName: String): WeekDay {
            return when (shortName) {
                "MO" -> MONDAY
                "TU" -> TUESDAY
                "WE" -> WEDNESDAY
                "TH" -> THURSDAY
                "FR" -> FRIDAY
                "SA" -> SATURDAY
                "SU" -> SUNDAY
                else -> throw IllegalArgumentException("$shortName is not a valid short name for WeekDay")
            }
        }

        fun byNumber(number: Int): WeekDay {
            return when (number) {
                1 -> SUNDAY
                2 -> MONDAY
                3 -> TUESDAY
                4 -> WEDNESDAY
                5 -> THURSDAY
                6 -> FRIDAY
                7 -> SATURDAY
                else -> throw IllegalArgumentException("$number is not a valid number for WeekDay")
            }
        }
    }
}