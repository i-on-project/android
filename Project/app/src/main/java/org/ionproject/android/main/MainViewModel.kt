package org.ionproject.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.ionproject.android.common.connectivity.IConnectivityObservable

private typealias onConnectionChanged = () -> Unit

class MainViewModel(
    private val connectivityObservable: IConnectivityObservable
) : ViewModel() {
    private var firstConnectivityChecked: Boolean = false

    // Check if there is a network connection
    fun hasConnectivity(): Boolean {
        val connectivity = connectivityObservable.hasConnectivity()
        firstConnectivityChecked = true
        return connectivity
    }

    // Observe changes to network connectivity
    fun observeConnectivity(onConnectionChanged: onConnectionChanged): Unit =
        connectivityObservable.observe {
            viewModelScope.launch(Dispatchers.Main) {
                onConnectionChanged()
            }
        }

    // Returns information about if connectivity has been checked when application has started
    fun hasConnectivityBeenChecked(): Boolean = firstConnectivityChecked
}