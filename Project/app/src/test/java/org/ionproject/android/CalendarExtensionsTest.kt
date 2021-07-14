package org.ionproject.android

import junit.framework.Assert.assertEquals
import org.ionproject.jdcalendar.weekDaysUntil
import org.junit.Test
import java.util.*

class CalendarExtensionsTest {

    @Test
    fun weekDaysUntil_isCorrect() {
        val today = Calendar.getInstance().apply { set(2020,5,1) }
        val nextMonth = Calendar.getInstance().apply { set(2020,6,7) }

        val count = today.weekDaysUntil(nextMonth)

        assertEquals(5, count)
    }
}