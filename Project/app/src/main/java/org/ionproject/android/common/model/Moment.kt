package org.ionproject.android.common.model

import java.util.*


/**
 * Helper class used to avoid having intervals hardcoded into the layout.
 * Represents a moment in time within a day. Only contains hours and minutes.
 * Example: 23:00.
 *
 * Performs time overflowing: 23:00 + 1:30 = 0:30.
 *
 * Accepts the following operators:
 * +, - , >, <, >=, <=, ==
 *
 * It can also be iterated like so:
 * (Moment(8:30) until Moment(23:00) step Moment(0,30)).foreach {
 *     // Will iterate from 8:30 to 23:00 incrementing 0:30 each iteration
 * }
 *
 */
data class Moment(val hours: Int, val minutes: Int) : Comparable<Moment> {

    override fun toString(): String {

        val stringHours = if (hours > 9) "$hours" else "0$hours"
        val stringMinutes = if (minutes > 9) "$minutes" else "0$minutes"

        return "$stringHours.$stringMinutes"
    }

    override fun equals(other: Any?): Boolean {
        val other = other as Moment
        return other.hours == hours && other.minutes == minutes
    }

    override fun compareTo(other: Moment): Int {
        if (hours == other.hours && minutes == other.minutes)
            return 0
        if (hours > other.hours || hours == other.hours && minutes > other.minutes)
            return 1
        return -1
    }

    init {
        if (hours < 0 || hours > 23)
            throw IllegalArgumentException("Hours must be below 23 and cannot have negative values")
        if (minutes < 0 || minutes > 59)
            throw IllegalArgumentException("Hours must be below 60 and connot have negative values")
    }

    companion object {
        fun fromCalendar(calendar: Calendar): Moment {
            return Moment(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }

        val OneHour = Moment(1, 0)
        val ThirtyMinutes = Moment(0, 30)
        val OneMinute = Moment(0, 1)
    }

    operator fun plus(moment: Moment): Moment {
        var newHours = hours + moment.hours
        if (newHours > 23) {
            if (newHours == 24)
                newHours = 0
            else
                newHours = newHours - 23
        }

        var newMinutes = minutes + moment.minutes
        if (newMinutes > 59) {
            newHours += 1
            if (newHours == 24)
                newHours = 0
            newMinutes -= 60
        }

        return Moment(newHours, newMinutes)
    }

    infix fun until(moment: Moment) =
        MomentProgression(
            this,
            moment
        )

    class MomentIterator(
        val startMoment: Moment,
        val endMomentInclusive: Moment,
        val stepMoment: Moment
    ) : Iterator<Moment> {
        private var currentMoment = startMoment

        override fun hasNext() = currentMoment <= endMomentInclusive

        override fun next(): Moment {

            val next = currentMoment

            currentMoment += stepMoment

            return next
        }
    }

    class MomentProgression(
        override val start: Moment,
        override val endInclusive: Moment,
        val stepMoment: Moment = OneMinute
    ) :
        Iterable<Moment>, ClosedRange<Moment> {

        override fun iterator(): Iterator<Moment> =
            MomentIterator(
                start,
                endInclusive,
                stepMoment
            )

        infix fun step(moment: Moment): List<Moment> {
            val it = MomentIterator(
                start,
                endInclusive,
                moment
            )
            val momentList = mutableListOf<Moment>()
            it.forEach {
                momentList.add(it)
            }
            return momentList
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