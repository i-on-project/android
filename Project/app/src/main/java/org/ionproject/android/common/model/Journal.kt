package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.JournalDto
import java.util.*

class Journal(
    uid: String,
    summary: String?,
    description: String?,
    val lastModification: Calendar?
) : Event("Journal", uid, summary, description)

fun JournalDto.toJournal() = createJournal(properties)

fun createJournal(properties: ComponentProperties) =
    Journal(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        lastModification = properties.dtstamp?.value?.get(0)?.toCalendar()
    )