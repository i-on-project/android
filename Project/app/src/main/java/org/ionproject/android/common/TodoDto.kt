package org.ionproject.android.common

import org.ionproject.android.common.siren.Todo
import java.net.URI

class TodoSummary(
    val uid: String,
    val summary: String?,
    val description: String?,
    val attachment: URI?,
    //TODO: Change due date for a object of Date type ex: 2020-03-19 23:59:59
    val due: String? = "????/??/?? ??:??:??"
)

fun Todo.toTodoSummary() =
    TodoSummary(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        attachment = URI(properties.attachment?.value?.get(0) ?: ""),
        due = properties.due?.value?.get(0)
    )
