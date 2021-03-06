package org.ionproject.android.common.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build

object ConnectivityObservableFactory {

    /**
     * Creates a different implementation of the [IConnectivityObservable] interface
     * according to the android SDK Version
     *
     * From API level 24 upwards the implementation uses the Network Callbacks
     * https://developer.android.com/reference/android/net/ConnectivityManager.NetworkCallback,
     * because [ConnectivityManager.getActiveNetworkInfo] is deprecated. Below API level 24 it
     * uses a BroadcastReceiver.
     */
    fun create(context: Context): IConnectivityObservable =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            ConnectivityObservable(context)
        else
            LegacyConnectivityObservable(context)
}