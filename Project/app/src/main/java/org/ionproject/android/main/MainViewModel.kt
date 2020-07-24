package org.ionproject.android.main

import androidx.lifecycle.ViewModel
import org.ionproject.android.common.connectivity.IConnectivityObservable

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