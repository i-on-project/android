package org.ionproject.android.common.connectivity


/**
 * Implementation of this interface should observe the device connectivity
 */
interface IConnectivityObservable {

    /**
     * Starts observing the connectivity
     * calls [onConnectionLost] when the connectivity
     * is lost.
     */
    fun observe(onConnectionLost: () -> Unit)

    /**
     * Should stop observing the connectivity
     */
    fun stopObserving()

    /**
     * Checks if there is connectivity available immediately
     */
    fun hasConnectivity(): Boolean
}