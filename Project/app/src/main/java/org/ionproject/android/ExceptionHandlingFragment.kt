package org.ionproject.android

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.ionproject.android.common.IonApplication

abstract class ExceptionHandlingFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        IonApplication.globalExceptionHandler.registerCurrExceptionHandler { thread, throwable ->
            exceptionHandler()
        }
    }

    open fun exceptionHandler() {
        findNavController().navigateUp()
        IonApplication.crashlytics.log("Getting information from Web API threw a 404 Not Found Status Code.")
        Toast.makeText(
            this.context,
            this.resources.getString(R.string.error_message_exception_handler_fragment),
            Toast.LENGTH_SHORT
        ).show()
    }
}