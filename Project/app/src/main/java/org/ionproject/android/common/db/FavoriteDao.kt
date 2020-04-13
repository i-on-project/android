package org.ionproject.android.common.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.ClassSummary

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(classSummary: ClassSummary)

    /*
    This method needs to return a livedata so that when an element is removed from the favorites list,
    the UI is updated without having to perform a new database request explicitly.
     */
    @Query("SELECT * FROM Favorite WHERE calendar_term = :calendarTerm")
    fun findFavoritesFromCalendarTerm(calendarTerm: String): LiveData<List<ClassSummary>>

    @Delete
    suspend fun deleteFavorite(classSummary: ClassSummary): Int

    @Query("SELECT COUNT(*) FROM Favorite WHERE course = :course AND calendar_term=:calendarTerm AND name=:classSection")
    suspend fun favoriteExists(course: String, calendarTerm: String, classSection: String): Int

}