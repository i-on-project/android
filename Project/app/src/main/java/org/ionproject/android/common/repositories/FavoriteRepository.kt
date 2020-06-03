package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Favorite

/**
 * This type represents a Favorite repository, it performs requests
 * to the local database.
 *
 * All methods run the coroutines with the [Dispatchers.IO]
 * which is optimized to perform disk or network I/O outside of the main thread.
 */
class FavoriteRepository(private val favoriteDao: FavoriteDao, private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    /**
     * Adds a favorite to the local database
     *
     * @param classSummary is the class to add to favorites
     */
    suspend fun addClassToFavorites(classSummary: ClassSummary) =
        withContext(dispatcher) {
            favoriteDao.insertFavorite(
                Favorite(
                    classSummary.id,
                    classSummary.courseAcronym,
                    classSummary.calendarTerm,
                    classSummary.detailsUri,
                    classSummary.selfUri
                )
            )
        }


    /**
     * Remove an existing favorite from the local database
     *
     * @param favorite is the favorite to remove
     */
    suspend fun removeFavorite(favorite: Favorite) =
        withContext(dispatcher) {
            favoriteDao.deleteFavorite(favorite)
        }

    suspend fun removeClassFromFavorites(classSummary: ClassSummary) {
        withContext(dispatcher) {
            favoriteDao.deleteFavorite(
                Favorite(
                    classSummary.id,
                    classSummary.courseAcronym,
                    classSummary.calendarTerm,
                    classSummary.detailsUri,
                    classSummary.selfUri
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
     * Checks if a favorite exist
     */
    suspend fun isClassFavorite(classSummary: ClassSummary): Boolean {
        if (favoriteDao.favoriteExists(
                classSummary.courseAcronym,
                classSummary.calendarTerm,
                classSummary.id
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
        withContext(dispatcher) {
            favoriteDao.insertFavorite(favorite)
        }
    }

}