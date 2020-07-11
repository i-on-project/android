package org.ionproject.android.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ionproject.android.common.model.*

@Database(
    entities = arrayOf(
        Root::class,
        ClassCollectionFields::class,
        ClassSummary::class,
        ClassSection::class,
        Suggestion::class,
        BackgroundWorker::class,
        Programme::class,
        ProgrammeSummary::class,
        ProgrammeOffer::class,
        ProgrammeOfferSummary::class,
        Course::class,
        CalendarTerm::class,
        Favorite::class,
        EventsFields::class,
        Exam::class,
        Todo::class,
        Journal::class,
        Lecture::class
    ), version = 1,
    exportSchema = true
)
@TypeConverters(
    URIConverter::class,
    CalendarConverter::class,
    MomentConverter::class,
    WeekDayConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rootDao(): RootDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun classSectionDao(): ClassSectionDao
    abstract fun workerDao(): WorkerDao
    abstract fun programmeDao(): ProgrammeDao
    abstract fun programmeOfferDao(): ProgrammeOfferDao
    abstract fun courseDao(): CourseDao
    abstract fun classCollectionDao(): ClassCollectionDao
    abstract fun calendarTermDao(): CalendarTermDao
    abstract fun eventsDao(): EventsDao
}