package org.ionproject.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.connectivity.IConnectivityObservable
import org.ionproject.android.common.repositories.FavoriteRepository
import org.ionproject.android.common.repositories.MainRepository
import java.net.URI

private typealias onConnectionChanged = () -> Unit

class MainViewModel(
    private val connectivityObservable: IConnectivityObservable,
    private val mainRepository: MainRepository
) : ViewModel() {

    /**
     * Signals the first connectivity check
     * Necessary for when the app opens and
     * there is no connectivity on the phone
     */
    private var firstConnectivityChecked: Boolean = false

    // Observe changes to network connectivity
    fun observeConnectivity(onConnectionChanged: onConnectionChanged) {
        if (!firstConnectivityChecked && !connectivityObservable.hasConnectivity()) {
            firstConnectivityChecked = true
            onConnectionChanged()
        }
        // Because NetworkCallbacks are not executed on the UI thread we use coroutines to do so.
        connectivityObservable.observe {
            firstConnectivityChecked = true
            onConnectionChanged()
        }
    }

    /**
     * This action has to be performed when the MainActivity launches
     * so it is automatic and doesn't require the user to enter the
     * Favorites fragment in order to trigger it
     */
    fun syncLocalFavoritesWithRemoteFavorites(){
        viewModelScope.launch {
            mainRepository.syncLocalFavoritesWithRemoteFavorites(URI(""))
        }
    }
}