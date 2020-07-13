package org.ionproject.android.error

import com.google.firebase.crashlytics.FirebaseCrashlytics

typealias ExceptionHandler = (Thread, Throwable) -> Unit

/**
 * Handles all exceptions thrown within the application.
 *
 * [defaultExceptionHandler] is the default exception handler
 *
 * [currExceptionHandler] is the handler that can be specified by a component (e.g Fragment)
 *
 * All exceptions are recorded to Crashlitycs.
 */
class GlobalExceptionHandler(
    val defaultExceptionHandler: ExceptionHandler
) {
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()


    /**
     * Overriding the defaultExceptionHandler which closes the aplication
     */
    init {
        val prevDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
            try {
                currExceptionHandler?.invoke(t, e) ?: defaultExceptionHandler(t, e)
                // Sends information about an "non-fatal" exception to crashlytics, e.g. caught exceptions like this
                crashlytics.recordException(e)
            } catch (ex: Exception) {
                // An exception occurred when executing one of the handler so close
                // the application and record exception
                if (prevDefaultExceptionHandler != null)
                    prevDefaultExceptionHandler.uncaughtException(t, ex)
                else {
                    crashlytics.recordException(ex)
                    android.os.Process.killProcess(android.os.Process.myPid())
                    System.exit(1)
                }
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

    fun sendAllExceptionsToFirebase() = crashlytics.sendUnsentReports()
}