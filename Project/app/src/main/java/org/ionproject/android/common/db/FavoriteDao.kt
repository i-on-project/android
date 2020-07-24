package org.ionproject.android.common.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT DISTINCT calendarTerm FROM Favorite")
    suspend fun getCalendarTermsFromFavorites(): List<String>

    /*
    This method needs to return a livedata so that when an element is removed from the favorites list,
    the UI is updated without having to perform a new database request explicitly.
     */
    @Query("SELECT * FROM Favorite WHERE calendarTerm = :calendarTerm")
    fun findFavoritesFromCalendarTerm(calendarTerm: String): LiveData<List<Favorite>>

    @Query("SELECT * FROM Favorite WHERE calendarTerm = :calendarTerm")
    suspend fun suspendFindFavoritesFromCalendarTerm(calendarTerm: String): List<Favorite>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite): Int

    @Query("SELECT COUNT(*) FROM Favorite WHERE courseAcronym = :course AND calendarTerm=:calendarTerm AND id=:classSection")
    suspend fun favoriteExists(course: String, calendarTerm: String, classSection: String): Int

    @Query("SELECT * FROM Favorite")
    suspend fun getAllFavorites(): List<Favorite>

}