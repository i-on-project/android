package org.ionproject.android.common.model

import org.ionproject.android.common.dto.Journal
import java.util.*

class JournalSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val lastModification: Calendar?
)

fun Journal.toJournalSummary() =
    JournalSummary(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        lastModification = properties.dtstamp?.value?.get(0)?.toCalendar()
    )