package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.ionproject.android.common.model.*
import java.net.URI

@Dao
abstract class EventsDao {

    @Transaction
    @Query("SELECT * FROM EventsFields WHERE selfUri = :uri")
    abstract suspend fun getEventsByUri(uri: URI): Events?

    suspend fun insertEvents(events: Events) {
        insertEventsFields(events.fields)
        insertExams(events.exams)
        insertJournals(events.journals)
        insertLectures(events.lectures)
        insertTodos(events.todos)
    }

    suspend fun deleteEventsByUri(uri: URI) {
        deleteEventsFieldsByUri(uri)
        deleteExamByUri(uri)
        deleteJournalByUri(uri)
        deleteLectureByUri(uri)
        deleteTodoByUri(uri)
    }

    @Insert
    abstract suspend fun insertExams(exams: List<Exam>)

    @Insert
    abstract suspend fun insertTodos(todos: List<Todo>)

    @Insert
    abstract suspend fun insertLectures(lecturee: List<Lecture>)

    @Insert
    abstract suspend fun insertJournals(journals: List<Journal>)

    @Insert
    abstract suspend fun insertEventsFields(eventsFields: EventsFields)

    @Query("DELETE FROM Exam WHERE selfUri = :uri")
    abstract suspend fun deleteExamByUri(uri: URI)

    @Query("DELETE FROM Todo WHERE selfUri = :uri")
    abstract suspend fun deleteTodoByUri(uri: URI)

    @Query("DELETE FROM Lecture WHERE selfUri = :uri")
    abstract suspend fun deleteLectureByUri(uri: URI)

    @Query("DELETE FROM Journal WHERE selfUri = :uri")
    abstract suspend fun deleteJournalByUri(uri: URI)

    @Query("DELETE FROM EventsFields WHERE selfUri = :uri")
    abstract suspend fun deleteEventsFieldsByUri(uri: URI)

}