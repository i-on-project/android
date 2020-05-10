package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.EventDto
import java.text.SimpleDateFormat
import java.util.*

class Exam(
    val uid: String,
    val summary: String?,
    val description: String?,
    val categories: String?,
    val created: Calendar?,
    val stamp: Calendar?,
    val startDate: Calendar?,
    val endDate: Calendar?
)

fun EventDto.toExam() = createExam(properties.properties)

fun createExam(properties: ComponentProperties) =
    Exam(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        categories = properties.categories?.value?.get(0),
        created = properties.created?.value?.get(0)?.toCalendar(),
        stamp = properties.dtstamp?.value?.get(0)?.toCalendar(),
        startDate = properties.dtstart?.value?.get(0)?.toCalendar(),
        endDate = properties.dtend?.value?.get(0)?.toCalendar()
    )

private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

fun String?.toCalendar(): Calendar? {
    if (this.isNullOrEmpty())
        return null

    val calendar = Calendar.getInstance()
    val date = formatter.parse(this)

    if (date != null) {
        calendar.time = date
        return calendar
    }

    return null
}
