package org.ionproject.android.common.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi

/**
 * [IObservableConnectivity] implementation used for devices where the Android SDK level is
 * 24 or above
 */
@RequiresApi(Build.VERSION_CODES.N)
class ObservableConnectivity(context: Context) : IObservableConnectivity {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityCallback? = null

    override fun observe(onConnectionLost: () -> Unit) {
        if (networkCallback != null)
            throw IllegalStateException("Already registered and observer, must unregister first")
        networkCallback = ConnectivityCallback(onConnectionLost)
        cm.registerDefaultNetworkCallback(networkCallback)
    }

    override fun stopObserving() {
        if (networkCallback != null)
            cm.unregisterNetworkCallback(networkCallback)
    }

    override fun hasConnectivity(): Boolean = cm.activeNetwork != null

    private inner class ConnectivityCallback(
        private val onConnectionLost: () -> Unit
    ) :
        ConnectivityManager.NetworkCallback() {

        override fun onLost(network: Network) {
            super.onLost(network)
            onConnectionLost()
        }
    }
}


