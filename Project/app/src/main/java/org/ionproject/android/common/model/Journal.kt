package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.util.*

/**
 * Event to be called as [Journal]
 */
class Journal(
    uid: String,
    summary: String,
    description: String,
    val lastModification: Calendar
) : Event("Journal", uid, summary, description)

/**
 * Creates an [Journal] event
 */
fun createJournal(properties: ComponentProperties): Journal {
    val uid = properties.uid.value[0]
    val summary = properties.summary.value[0]
    val description = properties.description.value.get(0)
    val lastModification = properties.dtstamp.value[0].toCalendar()

    if (lastModification != null) {
        return Journal(
            uid,
            summary,
            description,
            lastModification
        )
    }
    throw IllegalArgumentException("Found a null field while creating a journal")
}