package org.ionproject.android.error

import com.google.firebase.crashlytics.FirebaseCrashlytics

typealias ExceptionHandler = (Thread, Throwable) -> Unit

class GlobalExceptionHandler(
    val defaultExceptionHandler: ExceptionHandler
) {
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    init {
        Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
            try {
                currExceptionHandler?.invoke(t, e) ?: defaultExceptionHandler(t, e)
                // Sends information about an "non-fatal" exception to crashlytics, e.g. caught exceptions like this
                crashlytics.recordException(e)
            } catch (ex: Exception) {
                crashlytics.recordException(ex)
            }
        }
    }

    private var currExceptionHandler: ExceptionHandler? = null

    fun registerCurrExceptionHandler(exceptionHandler: ExceptionHandler) {
        currExceptionHandler = exceptionHandler
    }

    fun unRegisterCurrExceptionHandler() {
        currExceptionHandler = null
    }

}