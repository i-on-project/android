package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.ICalendarDto

class Lecture(
    val uid: String,
    val summary: String? = null,
    val description: String? = null,
    val start: String? = null,
    val duration: String? = null,
    val weekDay: String? = null
)

fun ICalendarDto.toCalendarSummary(): List<Lecture> =
    properties.subComponents.map { createLecture(it.properties) }

fun createLecture(properties: ComponentProperties) =
    Lecture(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        start = properties.dtstamp?.value?.get(0)?.getHours(),
        duration = properties.duration?.value?.get(0),
        weekDay = properties.rrule?.value?.byDay?.get(0)
    )

fun String.getHours(): String {
    val info = split("T")
    return info[1].substring(0, info[1].length - 1)
}