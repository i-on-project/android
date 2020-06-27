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
    val weekDay: WeekDay,
    // This is nullable because the lectures room might not have been announced
    val location: String?
) : Event(type, uid, summary, description) {

    companion object {
        const val type = "Lecture" //Type of the event
        const val color = "blue" //Color to be printed on calendar display as a small dot
    }
}

/**
 * Creates a [Lecture] event
 */
fun createLecture(properties: ComponentProperties): Lecture {

    val uid = properties.uid.value.firstOrNull()
    val summary = properties.summary.value.firstOrNull()
    val description = properties.description.value.firstOrNull()
    val startDate = properties.dtstart?.value?.firstOrNull()?.toCalendar()
    val endDate = properties.dtend?.value?.firstOrNull()?.toCalendar()
    val untilDate = properties.rrule?.getRuleByName("UNTIL").toCalendar()
    val weekDay = properties.rrule?.getRuleByName("BYDAY")
    val duration = if (endDate != null && startDate != null) endDate - startDate else null
    val location = properties.location?.value?.firstOrNull()

    if (uid != null && summary != null && description != null && startDate != null
        && untilDate != null && duration != null && weekDay != null
    ) {
        return Lecture(
            uid,
            summary,
            description,
            startDate,
            Moment(duration.hour, duration.minute),
            untilDate,
            WeekDay.byShortName(weekDay),
            location
        )
    }
    throw IllegalArgumentException("Found a null field while creating a lecture")

}


