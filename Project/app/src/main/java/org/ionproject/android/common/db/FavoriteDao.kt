package org.ionproject.android.common.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.Favorite

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Query("SELECT * FROM Favorite WHERE calendar_term = :calendarTerm")
    fun findFavoritesFromCalendarTerm(calendarTerm: String): LiveData<List<Favorite>>

    @Delete
    suspend fun deleteFavorite(favorite: Favorite) : Int

    @Query("SELECT COUNT(*) FROM Favorite WHERE course = :course AND calendar_term=:calendarTerm AND class_section=:classSection")
    suspend fun favoriteExists(course: String, calendarTerm: String, classSection: String) : Int

}