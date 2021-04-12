package org.ionproject.android.loading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_loading.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.error.ERROR_KEY
import org.ionproject.android.error.ErrorActivity
import org.ionproject.android.main.MainActivity
import java.net.URI

// Random value key used to pass the root object from [LoadingActivity] to [MainActivity] via the intent
const val ROOT_KEY = "m0192exe1gxe12x1"

/**
 * 1) Try and get data from the JsonHome URL
 * 2) if it fails, get the URL from the Remote Config file
 * 3) it it fails to get that info, check connectivity: open isel's page if it exists, redirect to no connect
 * error if it doesn't
 */
class LoadingActivity : ExceptionHandlingActivity() {

    private val loadingViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, LoadingViewModelProvider())[LoadingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        linearlayout_activity_loading.addGradientBackground()

        /**
         * If JsonHome contains all required resources then open main activity else app must be outdated
         *
         * We check if remote config live data is null bc this observer is used for all requests made to
         * the API: if it is the first request ever made, get the remote config
         * (the live data is null at this point); if it is the request after getting remote config,
         * and it fails again, redirect to ISEL's page
         *
         */
        loadingViewModel.observeRootLiveData(this) {
            when (it) {
                is FetchSuccess<Root> -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(ROOT_KEY, it.value)
                    this.startActivity(intent)
                }
                is FetchFailure<Root> -> {
                    if (loadingViewModel.getRemoteConfigLiveData() == null) {
                        loadingViewModel.getRemoteConfig()
                    }else {
                        openISELPage()
                    }
                }
            }
        }

        /**
         * We check the connectivity in this observer because, although unlikely, GitHub might
         * be down and there needs to be a plan for that
         */
        loadingViewModel.observeRemoteConfigLiveData(this){
            when (it) {
                is FetchSuccess<RemoteConfig> -> loadingViewModel.getJsonHome(URI(it.value.api_link))
                is FetchFailure<RemoteConfig> -> {
                    if(!IonApplication.connectivityObservable.hasConnectivity() ){
                        startActivity(
                            Intent(this, ErrorActivity::class.java)
                                .putExtra(
                                    ERROR_KEY,
                                    resources.getString(R.string.label_no_connectivity_loading_error)
                                )
                        )
                    }else{
                        openISELPage()
                    }
                }
            }
        }
    }

    //if the remote config link is the same as the link in the app,
    //it means the API is down and for now we redirect to the ISEL website
    private fun openISELPage(){
        val iselURL = "https://www.isel.pt"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(iselURL)))
    }

}
