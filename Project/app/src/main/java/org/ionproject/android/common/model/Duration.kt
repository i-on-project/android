package org.ionproject.android.common.model

class Duration(
    val hours: Int,
    val minutes: Int,
    val seconds: Int
)

/**
 * This method should fill with the character '0' if an int value is below or equal then 9.
 * This is useful to present a date value.
 * Date representation example: 08:00h - 09:00h
 */
fun Int.fillWithZero(): String =
    if (this <= 9) "0$this"
    else this.toString()