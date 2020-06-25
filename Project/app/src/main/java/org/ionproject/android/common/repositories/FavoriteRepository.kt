package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Favorite

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
     * @param classSection is the class to add to favorites
     */
    suspend fun addClassToFavorites(classSection: ClassSection) =
        withContext(Dispatchers.IO) {
            favoriteDao.insertFavorite(
                Favorite(
                    classSection.id,
                    classSection.courseAcronym,
                    classSection.calendarTerm,
                    classSection.selfUri
                )
            )
        }


    /**
     * Remove an existing favorite from the local database
     *
     * @param favorite is the favorite to remove
     */
    suspend fun removeFavorite(favorite: Favorite) =
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(favorite)
        }

    suspend fun removeClassFromFavorites(classSection: ClassSection) {
        withContext(Dispatchers.IO) {
            favoriteDao.deleteFavorite(
                Favorite(
                    classSection.id,
                    classSection.courseAcronym,
                    classSection.calendarTerm,
                    classSection.selfUri
                )
            )
        }
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
    fun getFavoritesFromTerm(calendarTerm: CalendarTerm): LiveData<List<Favorite>> =
        favoriteDao.findFavoritesFromCalendarTerm(calendarTerm.name)

    /**
     * Obtains all favorite from a calendar term
     *
     * @param curricularTerm the string representation of a calendar term
     * @return a LiveData list of favorites from a specific term
     */
    suspend fun suspendGetFavoritesFromTerm(calendarTerm: CalendarTerm) =
        withContext(Dispatchers.IO) {
            favoriteDao.suspendFindFavoritesFromCalendarTerm(calendarTerm.name)
        }

    /**
     * Checks if a favorite exist
     */
    suspend fun isClassFavorite(classSection: ClassSection): Boolean {
        if (favoriteDao.favoriteExists(
                classSection.courseAcronym,
                classSection.calendarTerm,
                classSection.id
            ) > 0
        )
            return true
        return false
    }

    /**
     * Adds a favorite to the local database
     *
     * @param favorite is the class to add to favorites
     */
    suspend fun addFavorite(favorite: Favorite) {
        withContext(Dispatchers.IO) {
            favoriteDao.insertFavorite(favorite)
        }
    }

}