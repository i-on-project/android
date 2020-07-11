package org.ionproject.android.common

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*

/**
 * This file follows the documentation:
 * https://developer.android.com/training/monitoring-device-state/connectivity-status-type#kotlin
 *
 * This method of checking for connectivity is deprecated from Android 10 (API Level 29) onwards.
 * Because we are using API Level 21 we cannot use the alternative which is
 * https://developer.android.com/reference/kotlin/android/net/ConnectivityManager.NetworkCallback
 */

/**
 * Checks if there is connectivity to the internet
 */
fun ConnectivityManager.hasConnectivity(): Boolean {
    val activeNetwork = this.activeNetworkInfo
    if (activeNetwork != null) {
        return when (activeNetwork.type) {
            ConnectivityManager.TYPE_WIFI -> true
            ConnectivityManager.TYPE_MOBILE -> true
            else -> false
        }
    }
    return false
}



typealias ConnectivityObserver = (Boolean) -> Unit

private const val CONNECTION_CHECK_PERIOD_MILLIS = 5000L

/**
 * IMPORTANT:
 * [ObservableConnectivity] is NOT thread-safe
 *
 * Only the UI Thread should call [observe] and [startObservingConnection]
 */
class ObservableConnectivity(
    context: Context
) {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var prevConnectionState: Boolean = connectivityManager.hasConnectivity()

    private val observers = mutableMapOf<LifecycleOwner, ConnectivityObserver>()

    private var coroutineScope: CoroutineScope? = null

    /**
     * [observer] WILL execute in the UI Thread, so it should only contain
     * UI related operations
     */
    fun observe(lifecycleOwner: LifecycleOwner, observer: ConnectivityObserver) {
        if (!observers.containsKey(lifecycleOwner)) {
            if (!prevConnectionState)
                observer(prevConnectionState)
            observers[lifecycleOwner] = observer
        }
    }

    fun startObservingConnection(coroutineScope: CoroutineScope) {
        if (this.coroutineScope != coroutineScope) {
            this.coroutineScope = coroutineScope
            coroutineScope.launch(Dispatchers.Default) {
                while (true) {
                    // Obtain new connection state
                    val newConnectionState = connectivityManager.hasConnectivity()

                    // If the new connection state has changed then notify observers
                    if (newConnectionState != prevConnectionState) {
                        prevConnectionState = newConnectionState

                        withContext(Dispatchers.Main) {
                            observers.forEach {
                                if (it.key.lifecycle.currentState != Lifecycle.State.DESTROYED)
                                    it.value.invoke(newConnectionState)
                            }
                        }
                    }
                    delay(CONNECTION_CHECK_PERIOD_MILLIS)
                }
            }
        }
    }
}