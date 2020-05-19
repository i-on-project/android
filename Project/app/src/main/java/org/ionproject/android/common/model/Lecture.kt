package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ICalendar
import org.ionproject.android.schedule.Moment

class Lecture(
    val uid: String,
    val summary: String? = null,
    val description: String? = null,
    val start: Moment? = null,
    val duration: Moment? = null,
    val day: String? = null
)

fun ICalendar.toCalendarSummary(): List<Lecture> =
    properties.subComponents.map {
        Lecture(
            uid = it.properties.uid.value[0],
            summary = it.properties.summary?.value?.get(0),
            description = it.properties.description?.value?.get(0),
            start = it.properties.dtstart?.value?.get(0)?.toMoment(),
            duration = it.properties.duration?.value?.get(0)?.toDurationMoment(),
            day = it.properties.rrule?.value?.byDay?.get(0)
        )
    }

/**
 * Maps from duration to moment
 */
private fun String?.toDurationMoment(): Moment? {
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
