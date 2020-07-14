package org.ionproject.android.error

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_error.*
import org.ionproject.android.R
import org.ionproject.android.common.addGradientBackground

// Random value key used to pass the error message to [ErrorActivity] via the intent
const val ERROR_KEY = "12xp3m91x0meh1"

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        linearlayout_activity_error.addGradientBackground()

        // Apply error message if it was passed via the intent
        intent.getStringExtra(ERROR_KEY)?.apply {
            textview_error_activity_message.text = this
        }

    }
}
