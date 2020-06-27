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
) : Event("Journal", uid, summary, description) {
    companion object {
        const val type = "journal"
    }
}

/**
 * Creates an [Journal] event
 */
fun createJournal(properties: ComponentProperties): Journal {
    val uid = properties.uid.value.firstOrNull()
    val summary = properties.summary.value.firstOrNull()
    val description = properties.description.value.firstOrNull()
    val lastModification = properties.dtstamp.value.firstOrNull().toCalendar()

    if (uid != null && summary != null && description != null && lastModification != null) {
        return Journal(
            uid,
            summary,
            description,
            lastModification
        )
    }
    throw IllegalArgumentException("Found a null field while creating a journal")
}