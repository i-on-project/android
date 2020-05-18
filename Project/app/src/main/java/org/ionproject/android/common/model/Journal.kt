package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.util.*

/**
 * Event to be called as [Journal]
 */
class Journal(
    uid: String,
    summary: String?,
    description: String?,
    val lastModification: Calendar?
) : Event("Journal", uid, summary, description)

/**
 * Creates an [Journal] event
 */
fun createJournal(properties: ComponentProperties) =
    Journal(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        lastModification = properties.dtstamp?.value?.get(0)?.toCalendar()
    )