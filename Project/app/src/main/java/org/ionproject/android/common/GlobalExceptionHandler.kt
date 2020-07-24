package org.ionproject.android.common

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import retrofit2.HttpException

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
class GlobalExceptionHandler {
    private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

    private var lastException: Throwable? = null

    // The handler with the most priority, always called if its not null
    private var currExceptionHandler: ExceptionHandler? = null

    // The base handler
    private var baseExceptionHandler: ExceptionHandler

    /* The default handler, the initial value of  [baseExceptionHandler] */
    private var defaultExceptionHandler: ExceptionHandler


    /**
     * Overriding the defaultExceptionHandler which closes the aplication
     */
    init {
        val prevDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        defaultExceptionHandler =
            if (prevDefaultUncaughtExceptionHandler != null) { t: Thread, e: Throwable ->
                prevDefaultUncaughtExceptionHandler.uncaughtException(t, e)
            } else { _: Thread, e: Throwable ->
                // Application MUST be closed
                crashlytics.recordException(e)
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(1)
            }
        baseExceptionHandler = defaultExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
            try {

                // Because the coroutines have an exception pre handler which calls this handler
                // it leads to in some cases the same exception being caught twice.
                // Its only occurring on the Xiaomi Redmi Note 7, on the emulator
                // the problem does not seem to occur.
                // TODO Revisit this approach and maybe consider abandoning it
                if (e === lastException) {
                    lastException = null
                    crashlytics.log("Exception $e was caught twice!")
                    crashlytics.recordException(e)
                } else {
                    // Logging request id when the
                    if (e is HttpException) {
                        val requestId = e.response()?.headers()?.get("Request-Id")
                        requestId?.let {
                            Log.e(
                                "GlobalExceptionHandler",
                                "Request with id $requestId caused an exception"
                            )
                            crashlytics.log("Request with id $requestId caused an exception")
                        }
                    }
                    lastException = e
                    // Sends information about an "non-fatal" exception to crashlytics, e.g. caught exceptions like this
                    Log.e("GlobalExceptionHandler", "$t threw $e", e)
                    crashlytics.recordException(e)
                    currExceptionHandler?.invoke(t, e) ?: baseExceptionHandler(t, e)
                }
            } catch (ex: Exception) {
                Log.e("GlobalExceptionHandler", "threw $ex", ex)
                defaultExceptionHandler(t, ex)
            }
        }
    }


    fun registerCurrExceptionHandler(exceptionHandler: ExceptionHandler) {
        currExceptionHandler = exceptionHandler
    }

    fun unRegisterCurrExceptionHandler() {
        currExceptionHandler = null
    }

    fun registerBaseExceptionHandler(exceptionHandler: ExceptionHandler) {
        baseExceptionHandler = exceptionHandler
    }

    fun unRegisterBaseExceptionHandler() {
        baseExceptionHandler = defaultExceptionHandler
    }
}