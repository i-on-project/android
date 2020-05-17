package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import java.net.URI
import java.util.*

class Todo(
    uid: String,
    summary: String?,
    description: String?,
    val attachment: URI?,
    val due: Calendar?
) : Event(type, uid, summary, description) {

    companion object {
        val type = "Todo"
        val color = "#FF8C00"
    }
}

fun createTodo(properties: ComponentProperties) =
    Todo(
        uid = properties.uid.value[0],
        summary = properties.summary?.value?.get(0),
        description = properties.description?.value?.get(0),
        attachment = URI(properties.attachment?.value?.get(0) ?: ""),
        due = properties.due?.value?.get(0)?.toCalendar()
    )