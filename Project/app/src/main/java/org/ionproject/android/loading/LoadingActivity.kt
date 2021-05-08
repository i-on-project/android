package org.ionproject.android.loading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_loading.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.IonApplication
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.common.model.Root
import org.ionproject.android.error.ERROR_ACTIVITY_EXCEPTION_EXTRA
import org.ionproject.android.error.ErrorActivity
import org.ionproject.android.main.MAIN_ACTIVITY_ROOT_EXTRA
import org.ionproject.android.main.MainActivity
import java.net.URI

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
         * If JsonHome contains all required resources then open main activity; if not app must be outdated
         *
         * if by the second JSONHome attempt this check fails, we move on the the normal usage but with
         * catalog info
         *
         * the json home intent is null and we use that null value to tell the main activity to
         * switch to catalog mode
         */
        loadingViewModel.observeRootLiveData(this) {
            val intent = Intent(this, MainActivity::class.java)
            when (it) {
                is FetchSuccess<Root> -> {
                    intent.putExtra(MAIN_ACTIVITY_ROOT_EXTRA, it.value)
                    this.startActivity(intent)
                }
                is FetchFailure<Root> -> {
                    if (loadingViewModel.getRemoteConfigLiveData() == null) {
                        loadingViewModel.getRemoteConfig()
                    } else {
                        this.startActivity(intent)
                    }
                }
            }
        }

        /**
         * We check the connectivity in this observer because, although unlikely, GitHub might
         * be down and there needs to be a plan for that
         *
         * If github is down, we don't continue with catalog info because those files
         * are held on github as well as the remote config
         */
        loadingViewModel.observeRemoteConfigLiveData(this) {
            when (it) {
                is FetchSuccess<RemoteConfig> -> loadingViewModel.getJsonHome(URI(it.value.api_link))
                is FetchFailure<RemoteConfig> -> {
                    if (!IonApplication.connectivityObservable.hasConnectivity()) {
                        startActivity(
                            Intent(this, ErrorActivity::class.java)
                                .putExtra(
                                    ERROR_ACTIVITY_EXCEPTION_EXTRA,
                                    resources.getString(R.string.label_no_connectivity_loading_error)
                                )
                        )
                    } else {
                        Toast.makeText(
                            this,
                            resources.getString(R.string.remote_config_unreachable),
                            Toast.LENGTH_LONG
                        ).show()
                        openISELPage()
                    }
                }
            }
        }
    }

    /**
     * if the remote config link is the same as the link in the app,
    it means the API is down and for now we redirect to the ISEL website
     */
    private fun openISELPage() {
        val iselURL = "https://www.isel.pt"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(iselURL)))
    }
}
