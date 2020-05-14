package org.ionproject.android.common.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.Suggestion

@Dao
interface SuggestionDAO {
    @Query("Select * from Suggestion")
    fun getAllSuggestions(): LiveData<List<Suggestion>>

    @Query("SELECT _ID, class_name FROM Suggestion where class_name LIKE :word")
    fun getSuggestions(word: String): List<Suggestion>

    @Insert
    suspend fun insertAll(vararg classSections: Suggestion)

    @Delete
    suspend fun delete(classSection: Suggestion)
}