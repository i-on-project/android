package org.ionproject.android.calendar.JDCalendar

/**
 * Represents a day of a month
 *
 * @property value is the integer value of the day (e.g the last day of month could be 31)
 * @property isDayOfCurrMonth returns true if the day is from the current month represented in the calendar
 * @property isToday return true if the day is today
 */
data class Day(
    val value: Int,
    val isDayOfCurrMonth: Boolean,
    val isToday: Boolean
)