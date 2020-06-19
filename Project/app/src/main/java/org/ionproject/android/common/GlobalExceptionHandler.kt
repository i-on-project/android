package org.ionproject.android.common

typealias ExceptionHandler = (Thread, Throwable) -> Unit

class GlobalExceptionHandler(
    val defaultExceptionHandler: ExceptionHandler
) {

    init {
        val oldHandler = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler { t: Thread, e: Throwable ->
            try {
                currExceptionHandler?.invoke(t, e) ?: defaultExceptionHandler(t, e)
                // TODO: Escrever no crashlytics
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
//                oldHandler?.uncaughtException(t, e)
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