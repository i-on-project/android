package org.ionproject.android.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.BackgroundWorker

@Database(
    entities = arrayOf(
        ClassSummary::class,
        ClassSection::class,
        Suggestion::class,
        BackgroundWorker::class
    ), version = 1
)
@TypeConverters(URIConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun suggestionDAO(): SuggestionDAO
    abstract fun favoriteDao(): FavoriteDao
    abstract fun classSectionDao(): ClassSectionDao
    abstract fun workerDao(): WorkerDao
}