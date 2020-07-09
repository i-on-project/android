package org.ionproject.android.common.model

import org.ionproject.android.common.dto.ComponentProperties
import org.ionproject.android.common.dto.EventProperties
import org.ionproject.android.common.dto.Language
import org.ionproject.android.common.dto.SirenICalendar

/**
 * This class contains all events that were received from a class section
 */
class Events(
    val exams: List<Exam>,
    val lectures: List<Lecture>,
    val todos: List<Todo>,
    val journals: List<Journal>
) {

    companion object {
        val NO_EVENTS = Events(emptyList(), emptyList(), emptyList(), emptyList())
    }
}

fun SirenICalendar.toEventsSummary(): Events {
    val exams = mutableListOf<Exam>()
    val lectures = mutableListOf<Lecture>()
    val todos = mutableListOf<Todo>()
    val journals = mutableListOf<Journal>()

    val components: List<EventProperties> = this.properties.subComponents

    components.forEach { component ->
        val type: String = component.type
        val properties: ComponentProperties = component.properties
        val categories: List<String>? =
            properties.categories.find { it.parameters?.language == Language.EN_GB }?.value

        when (type) {
            Event.type -> {
                if (categories?.find { it.contains(Lecture.type) } != null)
                    lectures.add(createLecture(properties))
                else
                    exams.add(createExam(properties))
            }
            Todo.type -> todos.add(createTodo(properties))
            Journal.type -> journals.add(createJournal(properties))
        }
    }
    return Events(exams, lectures, todos, journals)
}



