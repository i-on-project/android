package org.ionproject.android.common.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager


/**
 * [IObservableConnectivity] implementation used for devices where the Android SDK level is
 * below 24
 */
@Suppress("DEPRECATION")
class LegacyObservableConnectivity(private val context: Context) : IObservableConnectivity {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var receiver: ConnectivityBroadcastReceiver? = null

    override fun observe(onConnectionLost: () -> Unit) {
        if (receiver != null)
            throw IllegalStateException("Already registered and observer, must unregister first")
        receiver = ConnectivityBroadcastReceiver(cm, onConnectionLost)
        context.registerReceiver(receiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun stopObserving() {
        if (receiver != null)
            context.unregisterReceiver(receiver)
    }

    override fun hasConnectivity(): Boolean = cm.activeNetworkInfo?.isConnectedOrConnecting ?: false

    private inner class ConnectivityBroadcastReceiver(
        private val cm: ConnectivityManager,
        private val onConnectionLost: () -> Unit
    ) :
        BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            // on some devices ConnectivityManager.getActiveNetworkInfo() does not provide the correct network state
            // https://issuetracker.google.com/issues/37137911
            val networkInfo = cm.activeNetworkInfo
            val receiverNetworkInfo: NetworkInfo? =
                intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO)

            if (networkInfo != null && !networkInfo.isConnectedOrConnecting ||
                receiverNetworkInfo != null && !receiverNetworkInfo.isConnectedOrConnecting
            ) {
                onConnectionLost()
                return
            }
        }
    }
}