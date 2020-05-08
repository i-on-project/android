package org.ionproject.android.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ionproject.android.common.model.*

@Database(
    entities = arrayOf(
        ClassSummary::class,
        ClassSection::class,
        Suggestion::class,
        BackgroundWorker::class,
        Programme::class,
        ProgrammeSummary::class,
        ProgrammeOffer::class,
        ProgrammeOfferSummary::class
    ), version = 1
)
@TypeConverters(URIConverter::class, ResourceTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun suggestionDAO(): SuggestionDAO
    abstract fun favoriteDao(): FavoriteDao
    abstract fun classSectionDao(): ClassSectionDao
    abstract fun workerDao(): WorkerDao
    abstract fun programmeDao(): ProgrammeDao
    abstract fun programmeOfferDao(): ProgrammeOfferDao
}