package org.ionproject.android.common.model

import org.ionproject.android.common.dto.Event
import java.net.URI
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ExamSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val categories: String?,
    val created: LocalDateTime?,
    val stamp: LocalDateTime?,
    val startDate: LocalDateTime?,
    val endDate: LocalDateTime?,
    val link: URI?
)

fun Event.toExamSummary() = ExamSummary(
    uid = properties.properties.uid.value[0],
    summary = properties.properties.summary?.value?.get(0),
    description = properties.properties.description?.value?.get(0),
    categories = properties.properties.categories?.value?.get(0),
    created = properties.properties.created?.value?.get(0)?.toLocalDate(),
    stamp = properties.properties.dtstamp?.value?.get(0)?.toLocalDate(),
    startDate = properties.properties.dtstart?.value?.get(0)?.toLocalDate(),
    endDate = properties.properties.dtend?.value?.get(0)?.toLocalDate(),
    link = links.first().href
)

private val formatter = DateTimeFormatter.ISO_INSTANT

fun String.toLocalDate() : LocalDateTime {
    val instant = Instant.from(formatter.parse(this))
    return LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
}
