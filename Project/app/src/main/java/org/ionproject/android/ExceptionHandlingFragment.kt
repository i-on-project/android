package org.ionproject.android

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fasterxml.jackson.core.JsonProcessingException
import org.ionproject.android.common.IonApplication
import retrofit2.HttpException
import java.io.IOException

abstract class ExceptionHandlingFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        IonApplication.globalExceptionHandler.registerCurrExceptionHandler { _, throwable ->
            exceptionHandler(throwable)
        }
    }

    open fun exceptionHandler(throwable: Throwable) {
        findNavController().navigateUp()

        val errorMessage = when (throwable) {
            is JsonProcessingException -> this.resources.getString(R.string.error_message_exception_handler_fragment)
            is HttpException -> this.resources.getString(R.string.error_message_exception_resource_not_available_handler_fragment)
            is IOException -> this.resources.getString(R.string.error_message_exception_no_connectivity_handler_fragment)
            else -> this.resources.getString(R.string.error_message_exception_handler_fragment)
        }

        Toast.makeText(this.context, errorMessage, Toast.LENGTH_SHORT).show()
    }
}