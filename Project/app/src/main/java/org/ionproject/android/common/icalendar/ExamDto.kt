package org.ionproject.android.common.icalendar

import java.net.URI

class ExamSummary(
    val id: Int,
    val summary: String,
    val description: String,
    val startDate: String,
    val endDate: String,
    val link: URI
)

fun Event.toExamSummary(): ExamSummary {
    val id = properties.uid.value
    val summary = properties.summary?.value
    val description = properties.description?.value
    val startDate = properties.dtstart.value
    val endDate = properties.dtend?.value
    val link = links.first().href

    return ExamSummary(
        id = id as Int,
        summary = summary.toString(),
        description = description.toString(),
        startDate = startDate.toString(),
        endDate = endDate.toString(),
        link = link
    )
}