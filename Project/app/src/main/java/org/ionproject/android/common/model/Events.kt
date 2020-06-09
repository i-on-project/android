package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.EventProperties
import org.ionproject.android.common.dto.ICalendarDto

/**
 * This class contains all events that were received from a class section
 */
class Events(
    val exams: List<Exam>,
    val lectures: List<Lecture>,
    val todos: List<Todo>,
    val journals: List<Journal>
)

fun ICalendarDto.toEventsSummary(): Events {
    val exams = mutableListOf<Exam>()
    val lectures = mutableListOf<Lecture>()
    val todos = mutableListOf<Todo>()
    val journals = mutableListOf<Journal>()

    val components: List<EventProperties> = this.properties.subComponents

    components.forEach { component ->
        val type: String? = component.type
        val properties: ComponentProperties = component.properties
        val categories: List<String>? = properties.categories?.value

        if (type != null && categories != null) {
            if (type == "event") {
                if (categories.contains(Lecture.type))
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
