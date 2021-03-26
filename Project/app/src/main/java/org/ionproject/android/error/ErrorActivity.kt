package org.ionproject.android.error

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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

    private val errorViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, ErrorViewModelProvider())[ErrorViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        linearlayout_activity_error.addGradientBackground()

        // Closes the error activity
        button_activity_error_close.setOnClickListener {
            finish()
        }

        //get the remote config data
        button_activity_error_tryAgain.setOnClickListener{
            errorViewModel.makeRequest()
        }

        // Apply error message if it was passed via the intent
        intent.getStringExtra(ERROR_KEY)?.apply {
            textview_error_activity_message.text = this
        }

        errorViewModel.observeErrorLiveData(this) {

            Log.d("pls", it)

            if (it == null) {
                Toast.makeText(this, "Check phone connection", Toast.LENGTH_LONG).show()
            } else {
                if (it == WEB_API_HOST) //the data is fresh which means the API is down
                    openingTheBrowser()
                else{ //the data was not fresh, we update the value stored in the Shared Preferences and try again
                    IonApplication.saveAPIURLToSharedPreferences(it)
                    startActivity(Intent(this, LoadingActivity::class.java))
                }
            }
        }
    }

    //if the remote config link is the same as the link in the app,
    //it means the API is down and for now we redirect to the ISEL website
    private fun openingTheBrowser(){
        val iselURL = "https://www.isel.pt"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(iselURL)))
    }
}
