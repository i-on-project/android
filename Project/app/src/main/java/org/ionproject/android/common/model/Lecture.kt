package org.ionproject.android.common.model

import org.ionproject.android.calendar.jdcalendar.hour
import org.ionproject.android.calendar.jdcalendar.minus
import org.ionproject.android.calendar.jdcalendar.minute
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
    val values = properties.rrule?.value?.split(";")

    val uid = properties.uid.value[0]
    val summary = properties.summary?.value?.get(0)
    val description = properties.description?.value?.get(0)
    val startDate = properties.dtstart?.value?.get(0)?.toCalendar()
    val endDate = properties.dtend?.value?.get(0)?.toCalendar()
    val untilDate = values?.get(1)?.split("=")?.get(1)?.toCalendar()
    val weekDay = values?.get(2)?.split("=")?.get(1)
    val duration = if (endDate != null && startDate != null) endDate - startDate else null

    if (summary != null && description != null && startDate != null && untilDate != null && duration != null && weekDay != null) {
        return Lecture(
            uid,
            summary,
            description,
            startDate,
            Moment(duration.hour, duration.minute),
            untilDate,
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


