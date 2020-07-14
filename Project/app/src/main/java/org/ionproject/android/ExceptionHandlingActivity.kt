package org.ionproject.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.dto.MappingFromSirenException
import org.ionproject.android.error.ERROR_KEY
import org.ionproject.android.error.ErrorActivity
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
        IonApplication.globalExceptionHandler.sendAllExceptionsToFirebase()
    }

    open fun exceptionHandler(throwable: Throwable) {
        val intent = Intent(this, ErrorActivity::class.java)
        when (throwable) {
            is IOException -> intent.putExtra(
                ERROR_KEY,
                resources.getString(R.string.label_no_connectivity_loading)
            )
            is MappingFromSirenException -> intent.putExtra(
                ERROR_KEY,
                resources.getString(R.string.label_outdated_app)
            )
        }
        this.startActivity(intent)
        finish()
    }


}