package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.util.*

/**
 * Event to be called as [Lecture]
 */
class Lecture(
    uid: String,
    summary: String,
    description: String,
    val start: Calendar,
    val duration: Moment,
    val endDate: Calendar,
    val weekDay: WeekDay
) : Event(type, uid, summary, description) {

    companion object {
        const val type = "Lecture" //Type of the event
        const val color = "blue" //Color to be printed on calendar display as a small dot
    }
}

/**
 * Creates an [Lecture] event
 */
fun createLecture(properties: ComponentProperties): Lecture {
    val uid = properties.uid.value[0]
    val summary = properties.summary?.value?.get(0)
    val description = properties.description?.value?.get(0)
    val start = properties.dtstart?.value?.get(0)?.toCalendar()
    val duration = properties.duration?.value?.get(0)?.toMoment()
    val endDate = properties.rrule?.value?.until?.toCalendar()
    val weekDay = properties.rrule?.value?.byDay?.get(0)

    if (summary != null && description != null && start != null && endDate != null && duration != null && weekDay != null) {
        return Lecture(
            uid,
            summary,
            description,
            start,
            duration,
            endDate,
            WeekDay.byShortName(weekDay)
        )
    }
    throw IllegalArgumentException("Found a null field while creating a lecture")

}

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


