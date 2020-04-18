package org.ionproject.android.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary

@Database(
    entities = arrayOf(
        ClassSummary::class,
        ClassSection::class
    ), version = 1
)
@TypeConverters(URIConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun FavoriteDao(): FavoriteDao
    abstract fun ClassSectionDao(): ClassSectionDao
}