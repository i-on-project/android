package org.ionproject.android.common

import org.ionproject.android.common.siren.Event
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
    uid = properties.properties.uid.value[0],
    summary = properties.properties.summary?.value?.get(0),
    description = properties.properties.description?.value?.get(0),
    categories = properties.properties.categories?.value?.get(0),
    created = properties.properties.created?.value?.get(0),
    stamp = properties.properties.dtstamp?.value?.get(0),
    startDate = properties.properties.dtstart?.value?.get(0),
    endDate = properties.properties.dtend?.value?.get(0),
    link = links.first().href
)
