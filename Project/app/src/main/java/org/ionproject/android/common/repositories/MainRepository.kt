package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.settings.Preferences
import org.ionproject.android.userAPI.UserAPIRepository
import org.ionproject.android.userAPI.models.TokenRevoke
import java.net.URI

/**
 * It was necessary to create a new repository because [FavoriteRepository]
 * requires a bunch of parameters that are instantiated in [IonApplication]
 * so it's easier to just pass the whole repo as a parameter
 */
class MainRepository(private val favoritesRepository: FavoriteRepository, private val userAPIRepository: UserAPIRepository, private val preferences: Preferences) {

    suspend fun syncLocalFavoritesWithRemoteFavorites(uri: URI){
        withContext(Dispatchers.IO){
            favoritesRepository.syncFavoritesListFromCore(uri)
        }
    }

    suspend fun revokeAccessToken(){
        withContext(Dispatchers.IO){
            preferences.getAccessToken()?.let { TokenRevoke(it) }?.let {
                userAPIRepository.revokeAccessToken(
                    it
                )
            }
        }
    }
}