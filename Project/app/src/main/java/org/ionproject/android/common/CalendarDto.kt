package org.ionproject.android.common

import org.ionproject.android.common.siren.ICalendar

class Lecture(
    val uid: String,
    val summary: String? = null,
    val description: String? = null,
    val start: String? = null,
    val duration: String? = null,
    val day: String? = null
)

fun ICalendar.toCalendarSummary(): List<Lecture> =
    properties.subComponents.map {
        Lecture(
            uid = it.properties.uid.value[0],
            summary = it.properties.summary?.value?.get(0),
            description = it.properties.description?.value?.get(0),
            start = it.properties.dtstamp?.value?.get(0)?.getHours(),
            duration = it.properties.duration?.value?.get(0),
            day = it.properties.rrule?.value?.byDay?.get(0)
        )
    }

fun String.getHours(): String {
    val info = split("T")
    return info[1].substring(0, info[1].length - 1)
}