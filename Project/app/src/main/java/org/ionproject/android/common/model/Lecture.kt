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
    val endDate: Calendar? = null,
    val duration: Duration? = null,
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
        duration = properties.duration?.value?.get(0)?.toDuration(),
        weekDay = properties.rrule?.value?.byDay?.get(0)
    )

/**
 * Extended method in order to map information about a [Lecture]'s duration
 * to an [Duration] class.
 * Every duration information should be with this format: PT03H:00M:00S
 */
fun String.toDuration(): Duration? {
    val duration = this.split("PT", "H", "M", "S")
    return Duration(
        Integer.valueOf(duration[1]),
        Integer.valueOf(duration[2]),
        Integer.valueOf(duration[3])
    )
}
