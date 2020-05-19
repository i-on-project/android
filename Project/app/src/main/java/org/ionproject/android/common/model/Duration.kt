package org.ionproject.android.common.model

/**
 * Class in order to parse String information about the duration on an [Lecture] class event.
 * Every [Lecture] should have how many hours, minutes and seconds the lecture will take.
 * e.g: PT03H00M:00S
 */
class Duration(
    var hours: Int = 0,
    var minutes: Int = 0,
    var seconds: Int = 0
) {
    /**
     * Adds a number of hours to this instance.
     * If hours be greater or equal then 24, we need to decrement 24 in order to present a valid hour for the user.
     * E.g. 00:00h and not 24:00h
     */
    fun addHours(hours: Int) {
        this.hours += hours
        if (this.hours >= 24)
            this.hours -= 24
    }

    /**
     * Adds a number of minutes to this instance.
     * If minutes be greater or equal then 60, we need to decrement 60 minutes and add 1 hour to this instance
     * in order to present a valid minute for the user.
     * E.g. 03:01h and not 02:61h
     */
    fun addMinutes(minutes: Int) {
        this.minutes += minutes
        if (this.minutes >= 60) {
            hours++
            this.minutes -= 60
        }
    }
}

/**
 * This method should fill with the character '0' if an int value is below or equal then 9.
 * This is useful to present a date value.
 * Date representation example: 08:00h - 09:00h
 */
fun Int.fillWithZero(): String =
    if (this <= 9) "0$this"
    else this.toString()