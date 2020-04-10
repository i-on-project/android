package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.Favorite

/**
 * This type represents a Favorite repository, ir performs requests
 * to the local database.
 *
 * All methods run the coroutines with the [Dispatchers.IO]
 * which is optimized to perform disk or network I/O outside of the main thread.
 */
class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    /**
     * Adds a favorite to the local database
     *
     * @param favorite is the favorite to add
     */
    suspend fun addFavorite(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.insertFavorite(favorite)
        }


    /**
     * Remove an existing favorite from the local database
     *
     * @param favorite is the favorite to remove
     */
    suspend fun removeFavorite(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            val result = favoriteDao.deleteFavorite(favorite)
            if(result == 0)
                throw Exception("NOT FUCKING DELETING YOU PRICK")
        }

    /**
     * Obtains all favorite from a calendar term
     *
     * @param curricularTerm the string representation of a calendar term
     * @return a list of favorites from a specific term
     */
    fun getFavoritesFromTerm(calendarTerm: CalendarTerm): LiveData<List<Favorite>> =
            favoriteDao.findFavoritesFromCalendarTerm("s${calendarTerm.name}")

    /**
     * Checks if a favorite exist
     */
    suspend fun favoriteExists(favorite : Favorite) : Boolean {
        if(favoriteDao.favoriteExists(favorite.course,favorite.calendarTerm,favorite.classSection) > 0)
            return true
        return false
    }


}