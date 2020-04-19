package org.ionproject.android.calendar

data class Day(
    val value : Int,
    val isDayOfCurrMonth : Boolean,
    val isToday : Boolean
)