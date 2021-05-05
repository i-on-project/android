package org.ionproject.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.core.JsonProcessingException
import org.ionproject.android.common.IonApplication
import org.ionproject.android.error.*
import java.io.IOException

abstract class ExceptionHandlingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IonApplication.globalExceptionHandler.registerBaseExceptionHandler { _, throwable ->
            exceptionHandler(throwable)
        }
    }

    /**
     * We must send all recorded exceptions to firebase because otherwise they will only be sent
     * after the user opens the application again
     */
    override fun onDestroy() {
        super.onDestroy()
        IonApplication.globalExceptionHandler.unRegisterBaseExceptionHandler()
    }

    open fun exceptionHandler(throwable: Throwable) {
        val intent = Intent(this, ErrorActivity::class.java)
        when (throwable) {
            is JsonProcessingException -> intent.putExtra(
                ERROR_ACTIVITY_EXCEPTION_EXTRA,
                resources.getString(R.string.label_error_loading_error)
            )
            is IOException -> intent.putExtra(
                ERROR_ACTIVITY_EXCEPTION_EXTRA,
                resources.getString(R.string.label_no_connectivity_loading_error)
            )
            else -> intent.putExtra(
                ERROR_ACTIVITY_EXCEPTION_EXTRA,
                resources.getString(R.string.label_error_loading_error)
            )
        }
        this.startActivity(intent)
        finish()
    }


}
