package org.ionproject.android.common.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SuggestionDAO {
    @Query("SELECT * FROM Suggestion")
    fun getAll(): List<Suggestion>

    @Insert
    fun insertAll(vararg classSections: Suggestion)

    @Delete
    fun delete(classSection: Suggestion)
}