package org.ionproject.android.error

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_error.*
import org.ionproject.android.R
import org.ionproject.android.common.addGradientBackground

const val ERROR_ACTIVITY_EXCEPTION_EXTRA = "ErrorActivity.Exception.Extra"

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
        intent.getStringExtra(ERROR_ACTIVITY_EXCEPTION_EXTRA)?.apply {
            textview_error_activity_message.text = this
        }

    }
}
