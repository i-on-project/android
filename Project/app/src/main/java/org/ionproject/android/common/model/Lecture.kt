package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.util.*

/**
 * Event to be called as [Lecture]
 */
class Lecture(
    uid: String,
    summary: String? = null,
    description: String? = null,
    val start: Calendar? = null,
    val duration: Moment? = null,
    val endDate: Calendar? = null,
    val weekDay: String? = null
) : Event(type, uid, summary, description) {

    companion object {
        const val type = "Lecture" //Type of the event
        const val color = "blue" //Color to be printed on calendar display as a small dot
    }
}

/**
 * Creates an [Lecture] event
 */
fun createLecture(properties: ComponentProperties) =
    Lecture(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        start = properties.dtstart?.value?.get(0)?.toCalendar(),
        endDate = properties.rrule?.value?.until?.toCalendar(),
        duration = properties.duration?.value?.get(0)?.toMoment(),
        weekDay = properties.rrule?.value?.byDay?.get(0)
    )

private fun String?.toMoment(): Moment? {
    val hoursEndIdx = this?.indexOf("H")
    val minutesIdx = this?.indexOf("M")

    if (hoursEndIdx != null && minutesIdx != null) {
        val hours = this?.substring(hoursEndIdx - 2, hoursEndIdx)?.toInt()
        val minutes = this?.substring(minutesIdx - 2, minutesIdx)?.toInt()
        if (hours != null && minutes != null)
            return Moment(hours, minutes)
    }
    return null
}


