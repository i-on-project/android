package org.ionproject.android.common.repositories

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Favorite
import org.ionproject.android.settings.Preferences
import java.net.URI

/**
 * This type represents a Favorite repository, it performs requests
 * to the local database.
 *
 * All methods run the coroutines with the [Dispatchers.IO]
 * which is optimized to perform disk or network I/O outside of the main thread.
 */
class FavoriteRepository(
    private val favoriteDao: FavoriteDao,
    private val dispatcher: CoroutineDispatcher,
    private val webAPI: IIonWebAPI, //needed for the UserAPI class section actions
    private val preferences: Preferences
) {

    suspend fun addClassSectionToCoreFavorites(uri: URI) =  withContext(dispatcher) {
        webAPI.addClassSectionToCoreFavourites(uri, "Bearer ${preferences.getAccessToken()}")
    }

    /**
     * Adds a favorite to the local database
     *
     * @param classSection is the class to add to favorites
     */
    suspend fun addClassToFavorites(classSection: ClassSection) =
        withContext(dispatcher) {
            favoriteDao.insertFavorite(
                Favorite(
                    classSection.id,
                    classSection.courseAcronym,
                    classSection.calendarTerm,
                    classSection.selfUri
                )
            )
        }

    suspend fun removeClassSectionFromCoreFavorites(uri: URI) =  withContext(dispatcher) {
        webAPI.removeClassSectionFromCoreFavourites(uri, "Bearer ${preferences.getAccessToken()}")
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

    suspend fun removeClassFromFavorites(classSection: ClassSection) {
        withContext(dispatcher) {
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
        withContext(dispatcher) {
            favoriteDao.suspendFindFavoritesFromCalendarTerm(calendarTerm.name)
        }

    /**
     * Checks if a favorite exist
     */
    suspend fun isClassFavorite(classSection: ClassSection) =
        withContext(dispatcher) {
            if (favoriteDao.favoriteExists(
                    classSection.courseAcronym,
                    classSection.calendarTerm,
                    classSection.id
                ) > 0
            )
                return@withContext true
            return@withContext false
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

    suspend fun getAllFavorites(): List<Favorite> =
        withContext(dispatcher) {
            favoriteDao.getAllFavorites()
        }


}