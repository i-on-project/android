package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.EventProperties
import org.ionproject.android.common.dto.ICalendarDto
import org.ionproject.android.common.dto.enums.EventType
import java.util.*

class Events(
    var exams: List<Exam>,
    var lectures: List<Lecture>,
    var todos: List<Todo>,
    var journals: List<Journal>
)

fun ICalendarDto.toEventsSummary(): Events {
    val exams: MutableList<Exam> = mutableListOf()
    val lectures: MutableList<Lecture> = mutableListOf()
    val todos: MutableList<Todo> = mutableListOf()
    val journals: MutableList<Journal> = mutableListOf()

    val components: List<EventProperties> = this.properties.subComponents

    components.forEach { component ->
        val type: String? = component.type
        val properties: ComponentProperties = component.properties
        val categories: List<String>? = properties.categories?.value

        if (type != null && categories != null) {
            if (type == "event") {
                if (categories.contains(EventType.Lecture))
                    lectures.add(createLecture(properties))
                else
                    exams.add(createExam(properties))
            } else if (type == "todo")
                todos.add(createTodo(properties))
            else if (type == "journal")
                journals.add(createJournal(properties))
        }
    }

    return Events(exams, lectures, todos, journals)
}

fun List<String>.contains(type: EventType) =
    this.contains(type.toString())
