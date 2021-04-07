package org.ionproject.android.loading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_loading.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.error.ERROR_KEY
import org.ionproject.android.error.ErrorActivity
import org.ionproject.android.main.MainActivity

// Random value key used to pass the root object from [LoadingActivity] to [MainActivity] via the intent
const val ROOT_KEY = "m0192exe1gxe12x1"

class LoadingActivity : ExceptionHandlingActivity() {

    private val loadingViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, LoadingViewModelProvider())[LoadingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        linearlayout_activity_loading.addGradientBackground()

        // If JsonHome contains all required resources then open main activity else app must be outdated
        loadingViewModel.observeRootLiveData(this) {
            if (it != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(ROOT_KEY, it)
                this.startActivity(intent)
            } else {
                Log.i("pls","root was null")
                if(!loadingViewModel.fresh)
                    loadingViewModel.getRemoteConfig()
                else
                    openingTheBrowser()
            }
        }

        //by this point remoteConfig already has the valid link
        loadingViewModel.observeRemoteConfigLiveData(this){
            Log.i("pls","Remote Config returned")
            if(it != null)
                loadingViewModel.getJsonHome()
            else
                startActivity(
                    Intent(this,ErrorActivity::class.java)
                        .putExtra(ERROR_KEY, resources.getString(R.string.label_no_connectivity_loading_error)
                        )
                )
        }
    }

    //if the remote config link is the same as the link in the app,
    //it means the API is down and for now we redirect to the ISEL website
    private fun openingTheBrowser(){
        val iselURL = "https://www.isel.pt"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(iselURL)))
    }
}
