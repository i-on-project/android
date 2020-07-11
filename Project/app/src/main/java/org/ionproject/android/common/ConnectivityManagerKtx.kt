package org.ionproject.android.common

import android.net.ConnectivityManager

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