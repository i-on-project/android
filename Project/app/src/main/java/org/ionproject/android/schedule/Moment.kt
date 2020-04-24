package org.ionproject.android.schedule

class Moment(val hours: Int, val minutes: Int) : Comparable<Moment> {

    override fun toString(): String {

        val stringHours = if (hours > 9) "$hours" else "0$hours"
        val stringMinutes = if (minutes > 9) "$minutes" else "0$minutes"

        return "$stringHours.$stringMinutes"
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
        val OneHour = Moment(1, 0)
        val ThirtyMinutes = Moment(0, 30)
        val OneMinute = Moment(0, 1)
    }

    operator fun plus(moment: Moment): Moment {
        var newHours = hours + moment.hours
        if (newHours > 23) {
            val divVal = (newHours / 23).toInt()
            newHours = newHours - 23 * divVal
        }

        var newMinutes = minutes + moment.minutes
        if (newMinutes > 59) {
            newHours += 1
            newMinutes -= 60
        }

        return Moment(newHours, newMinutes)
    }

    infix fun until(moment: Moment) = MomentProgression(this, moment)

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
        val stepMoment: Moment = Moment.OneMinute
    ) :
        Iterable<Moment>, ClosedRange<Moment> {

        override fun iterator(): Iterator<Moment> =
            MomentIterator(start, endInclusive, stepMoment)

        infix fun step(moment: Moment): List<Moment> {
            val it = MomentIterator(start, endInclusive, moment)
            val momentList = mutableListOf<Moment>()
            it.forEach {
                momentList.add(it)
            }
            return momentList
        }
    }
}