package org.ionproject.android.common.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.ionproject.android.common.dto.*
import java.net.URI


/**
 * This type represents the information that is common
 * among all events from a class or class-section
 *
 * Its required to support the @EmbeddedEntity @Relation
 * representation from Room
 */
@Entity
data class EventsFields(
    @PrimaryKey val selfUri: URI,
    override var workerId: Int = 0
) : ICacheable

/**
 * This class contains all events that were received from a class section
 */
data class Events(
    @Relation(parentColumn = "selfUri", entityColumn = "selfUri", entity = Exam::class)
    val exams: List<Exam>,
    @Relation(parentColumn = "selfUri", entityColumn = "selfUri", entity = Lecture::class)
    val lectures: List<Lecture>,
    @Relation(parentColumn = "selfUri", entityColumn = "selfUri", entity = Todo::class)
    val todos: List<Todo>,
    @Relation(parentColumn = "selfUri", entityColumn = "selfUri", entity = Journal::class)
    val journals: List<Journal>,
    @Embedded val fields: EventsFields
) {

    companion object {
        val NO_EVENTS =
            Events(emptyList(), emptyList(), emptyList(), emptyList(), EventsFields(URI("")))

        fun create(
            exams: List<Exam>,
            lectures: List<Lecture>,
            todos: List<Todo>,
            journals: List<Journal>,
            selfUri: URI = URI("")
        ) = Events(exams, lectures, todos, journals, EventsFields(selfUri))
    }
}

fun SirenICalendar.toEventsSummary(): Events {
    val exams = mutableListOf<Exam>()
    val lectures = mutableListOf<Lecture>()
    val todos = mutableListOf<Todo>()
    val journals = mutableListOf<Journal>()
    val selfUri = this.links?.findByRel("self")
    val components: List<EventProperties> = this.properties.subComponents

    if (selfUri != null) {
        components.forEach { component ->
            val type: String = component.type
            val properties: ComponentProperties = component.properties
            val categories: List<String>? =
                properties.categories.find { it.parameters?.language == Language.EN_GB }?.value

            when (type) {
                Event.type -> {
                    if (categories?.find { it.contains(Lecture.type) } != null)
                        lectures.add(createLecture(properties, selfUri))
                    else
                        exams.add(createExam(properties, selfUri))
                }
                Todo.type -> todos.add(createTodo(properties, selfUri))
                Journal.type -> journals.add(createJournal(properties, selfUri))
            }
        }
        return Events.create(exams, lectures, todos, journals, selfUri)
    }
    throw MappingFromSirenException("Cannot convert $this to Events")
}



