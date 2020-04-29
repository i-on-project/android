package org.ionproject.android.common.icalendar

import java.net.URI

class ExamSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val categories: String?,
    val created: String?,
    val stamp: String?,
    val startDate: String?,
    val endDate: String?,
    val link: URI?
)

fun Event.toExamSummary() = ExamSummary(
        uid = properties.properties.uid.value,
        summary = properties.properties.summary?.value,
        description = properties.properties.description?.value,
        categories = properties.properties.categories?.value,
        created = properties.properties.created?.value,
        stamp = properties.properties.dtstamp?.value,
        startDate = properties.properties.dtstart?.value,
        endDate = properties.properties.dtend?.value,
        link = links.first().href
    )
