package org.ionproject.android.common

/**
getJsonHome() returns null when the API is unavailable. This makes the default null status of
rootLiveData ambiguous, so this class envelops the results of the getJsonHome() method in order to
properly evaluate the state of the API and to launch the Remote Config strat

Fetch Failure with null value: request was made to API and there was no response
Fetch Failure with throwable: request threw an Exception, if not caught triggers the global Exception Handler
from ExceptionHandlingActivity()

Fetch Success : valid response
 */
sealed class FetchResult<out T>
data class FetchFailure<T>(val throwable: Throwable? = null) : FetchResult<T>()
data class FetchSuccess<T>(val value: T) : FetchResult<T>()
