package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSummary

/**
 * This type represents a Favorite repository, it performs requests
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
    suspend fun addFavorite(classSummary: ClassSummary) =
        withContext(Dispatchers.IO) {
            favoriteDao.insertFavorite(classSummary)
        }


    /**
     * Remove an existing favorite from the local database
     *
     * @param favorite is the favorite to remove
     */
    suspend fun removeFavorite(classSummary: ClassSummary) =
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(classSummary)
        }

    /**
     * Obtains all favorite from a calendar term
     *
     * @param curricularTerm the string representation of a calendar term
     * @return a LiveData list of favorites from a specific term
     *
     * This method returns a livedata instead of being a suspend function because
     * this way its easy to remove items from table favorites and automatically update the UI
     */
    fun getFavoritesFromTerm(calendarTerm: CalendarTerm): LiveData<List<ClassSummary>> =
        favoriteDao.findFavoritesFromCalendarTerm(calendarTerm.name)

    /**
     * Checks if a favorite exist
     */
    suspend fun favoriteExists(classSummary: ClassSummary): Boolean {
        if (favoriteDao.favoriteExists(
                classSummary.course,
                classSummary.calendarTerm,
                classSummary.name
            ) > 0
        )
            return true
        return false
    }


}