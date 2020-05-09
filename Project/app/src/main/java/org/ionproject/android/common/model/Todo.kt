package org.ionproject.android.common.model

import org.ionproject.android.common.dto.Todo
import java.net.URI
import java.util.*

class TodoSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val attachment: URI?,
    val due: Calendar?
)

fun Todo.toTodoSummary() =
    TodoSummary(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        attachment = URI(properties.attachment?.value?.get(0) ?: ""),
        due = properties.due?.value?.get(0)?.toCalendar()
    )
