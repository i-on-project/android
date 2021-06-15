package org.ionproject.android.main

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchResult
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.IonApplication.Companion.preferences
import org.ionproject.android.common.connectivity.IConnectivityObservable
import org.ionproject.android.userAPI.UserAPIRepository
import org.ionproject.android.userAPI.models.PollResponse
import org.ionproject.android.userAPI.models.TokenRefresh

private typealias onConnectionChanged = () -> Unit

class MainViewModel(
    private val connectivityObservable: IConnectivityObservable
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
}