package org.ionproject.android.common.model

import org.ionproject.android.common.dto.Event
import org.ionproject.android.schedule.Moment
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class ExamSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val categories: String?,
    val created: Calendar?,
    val stamp: Calendar?,
    val startDate: Calendar?,
    val endDate: Calendar?,
    val link: URI?
)

fun Event.toExamSummary() = ExamSummary(
    uid = properties.properties.uid.value[0],
    summary = properties.properties.summary?.value?.get(0),
    description = properties.properties.description?.value?.get(0),
    categories = properties.properties.categories?.value?.get(0),
    created = properties.properties.created?.value?.get(0)?.toCalendar(),
    stamp = properties.properties.dtstamp?.value?.get(0)?.toCalendar(),
    startDate = properties.properties.dtstart?.value?.get(0)?.toCalendar(),
    endDate = properties.properties.dtend?.value?.get(0)?.toCalendar(),
    link = links.first().href
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


fun String?.toMoment(): Moment? {
    if (this.isNullOrEmpty())
        return null

    val calendar = Calendar.getInstance()
    val date = formatter.parse(this)

    if (date != null) {
        calendar.time = date
        return Moment(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    }

    return null
}
