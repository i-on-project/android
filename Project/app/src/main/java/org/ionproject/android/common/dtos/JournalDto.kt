package org.ionproject.android.common.dtos

import org.ionproject.android.common.siren.Journal

class JournalSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val lastModification: String?
)

fun Journal.toJournalSummary() =
    JournalSummary(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        lastModification = properties.dtstamp?.value?.get(0)
    )