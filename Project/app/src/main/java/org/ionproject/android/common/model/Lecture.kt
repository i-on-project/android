package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.ICalendarDto
import java.util.*

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
        val type = "Lecture"
        val color = "blue"
    }
}

fun ICalendarDto.toCalendarSummary(): List<Lecture> =
    properties.subComponents.map { createLecture(it.properties) }

fun createLecture(properties: ComponentProperties) =
    Lecture(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        start = properties.dtstamp?.value?.get(0)?.toCalendar(),
        endDate = properties.rrule?.value?.until?.toCalendar(),
        duration = properties.duration?.value?.get(0)?.toDuration(),
        weekDay = properties.rrule?.value?.byDay?.get(0)
    )

fun String.toDuration(): Duration? {
    val duration = this.split("PT", "H", "M", "S")
    return Duration(
        Integer.valueOf(duration[1]),
        Integer.valueOf(duration[2]),
        Integer.valueOf(duration[3])
    )
}
