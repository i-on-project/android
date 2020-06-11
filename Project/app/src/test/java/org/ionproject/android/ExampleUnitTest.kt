package org.ionproject.android

import org.ionproject.android.common.model.Moment
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val moment = Moment.ThirtyMinutes

        val newMoment = moment + Moment.ThirtyMinutes

        assertEquals(1, newMoment.hours)
    }
}
