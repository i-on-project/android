package org.ionproject.android.common.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Suggestion::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun suggestionDAO(): SuggestionDAO
}