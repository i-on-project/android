package org.ionproject.android.common.model

import org.ionproject.android.common.dto.Journal
import java.time.LocalDateTime

class JournalSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val lastModification: LocalDateTime?
)

fun Journal.toJournalSummary() =
    JournalSummary(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        lastModification = properties.dtstamp?.value?.get(0)?.toLocalDate()
    )