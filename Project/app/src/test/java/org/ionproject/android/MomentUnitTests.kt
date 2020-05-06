package org.ionproject.android

import org.ionproject.android.schedule.Moment
import org.junit.Test

import org.junit.Assert.*

class MomentUnitTests {

    @Test
    fun addition_isCorrect() {
        val moment = Moment.ThirtyMinutes + Moment.ThirtyMinutes + Moment.OneHour
        assertEquals(2, moment.hours)
    }

    @Test
    fun additionOverflow_isCorrect() {
        val moment = Moment(23,0)
        val newMoment = moment + Moment.OneHour
        assertEquals(0, newMoment.hours)
    }

    @Test
    fun until_isCorrect() {
        val start = Moment(1,0)
        val end = Moment(2,0)
        val momentsList = start until end
        assertEquals(61, momentsList.count())
    }

    @Test
    fun untilWithStep_isCorrect() {
        val start = Moment(8,0)
        val end = Moment(23,0)
        val momentsList = start until end step Moment.ThirtyMinutes
        assertEquals(31, momentsList.count())
    }
}