package org.ionproject.android.error

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_error.*
import org.ionproject.android.R
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.ionwebapi.WEB_API_HOST
import org.ionproject.android.loading.LoadingActivity
import org.ionproject.android.loading.LoadingViewModel
import org.ionproject.android.loading.LoadingViewModelProvider

// Random value key used to pass the error message to [ErrorActivity] via the intent
const val ERROR_KEY = "12xp3m91x0meh1"

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        linearlayout_activity_error.addGradientBackground()

        // Closes the error activity
        button_activity_error_close.setOnClickListener {
            finish()
        }

        // Apply error message if it was passed via the intent
        intent.getStringExtra(ERROR_KEY)?.apply {
            textview_error_activity_message.text = this
        }
    }
}
