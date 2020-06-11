package org.ionproject.android.error

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_error.*
import org.ionproject.android.R
import org.ionproject.android.common.addGradientBackground

class ErrorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        linearlayout_activity_error.addGradientBackground()
    }
}
