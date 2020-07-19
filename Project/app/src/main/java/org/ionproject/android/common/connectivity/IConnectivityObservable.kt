package org.ionproject.android.common.connectivity

import androidx.annotation.MainThread


/**
 * Implementation of this interface should observe the device connectivity
 */
interface IConnectivityObservable {

    /**
     * Starts observing the connectivity
     * calls [onConnectionLost] on the UI Thread when the connectivity
     * is lost.
     */
    @MainThread
    fun observe(onConnectionLost: () -> Unit)

    /**
     * Should stop observing the connectivity
     */
    @MainThread
    fun stopObserving()

    /**
     * Checks if there is connectivity available immediately
     */
    @MainThread
    fun hasConnectivity(): Boolean
}