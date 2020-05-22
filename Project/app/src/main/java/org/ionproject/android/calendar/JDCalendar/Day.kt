package org.ionproject.android.calendar.jdcalendar

import java.util.*

/**
 * Represents a day of a month
 *
 * @property value is the calendar information about that day
 * @property isDayOfCurrMonth is true if the day is from the current month represented in the calendar
 * @property isToday is true if the day is today
 * @property isAfterToday is true if the day if after today
 */
data class Day(
    val value: Calendar,
    val isDayOfCurrMonth: Boolean,
    val isToday: Boolean,
    val isAfterToday: Boolean
) {
    override operator fun equals(other: Any?): Boolean =
        other is Calendar &&
                value.day == other.day &&
                value.month == other.month &&
                value.year == other.year

    fun isBefore(endDate: Calendar): Boolean =
        value.before(endDate)
}