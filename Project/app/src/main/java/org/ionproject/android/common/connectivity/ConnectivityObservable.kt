package org.ionproject.android.common.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi

/**
 * [IConnectivityObservable] implementation used for devices where the Android SDK level is
 * 24 or above
 */
@RequiresApi(Build.VERSION_CODES.N)
class ConnectivityObservable(context: Context) : IConnectivityObservable {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityCallback? = null
    private val mainHandler by lazy(LazyThreadSafetyMode.NONE) { Handler(Looper.getMainLooper()) }

    override fun observe(onConnectionLost: () -> Unit) {
        if (networkCallback != null)
            throw IllegalStateException("Already registered an observed, must unregister first")
        networkCallback = ConnectivityCallback(onConnectionLost)
        cm.registerDefaultNetworkCallback(networkCallback as ConnectivityManager.NetworkCallback)
    }

    override fun stopObserving() {
        cm.unregisterNetworkCallback(networkCallback as ConnectivityManager.NetworkCallback)
        networkCallback = null

    }

    override fun hasConnectivity(): Boolean {
        val network = cm.activeNetwork ?: return false
        val networkCapabilities = cm.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }

    private inner class ConnectivityCallback(
        private val onConnectionLost: () -> Unit
    ) :
        ConnectivityManager.NetworkCallback() {

        // Method executes on the Connectivity Thread (NOT the UI Thread)
        override fun onLost(network: Network) {
            super.onLost(network)
            // Dispatches execution of onConnectionLost on the MainThread
            mainHandler.post(onConnectionLost)
        }
    }
}


