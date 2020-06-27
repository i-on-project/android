package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.net.URI
import java.util.*

/**
 * Event to be called as [Todo]
 */
class Todo(
    uid: String,
    summary: String,
    description: String,
    val attachment: URI,
    val due: Calendar
) : Event(type, uid, summary, description) {

    companion object {
        const val type = "todo"
        const val color = "#FF8C00"
    }
}

/**
 * Creates a [Todo] event
 */
fun createTodo(properties: ComponentProperties): Todo {
    val uid = properties.uid.value.firstOrNull()
    val summary = properties.summary.value.firstOrNull()
    val description = properties.description.value.firstOrNull()
    val attachment = URI(properties.attachment?.value?.firstOrNull() ?: "")
    val due = properties.due?.value?.firstOrNull()?.toCalendar()

    if (uid != null && summary != null && description != null && due != null) {
        return Todo(
            uid,
            summary,
            description,
            attachment,
            due
        )
    }
    throw IllegalArgumentException("Found a null field while creating a todo")
}
