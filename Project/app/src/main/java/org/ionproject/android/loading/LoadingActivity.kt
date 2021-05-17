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
import org.ionproject.android.offline.CatalogMainActivity
import java.net.URI

/**
 * 1) Try and get data from the JsonHome URL
 * 2) if it fails, check connectivity
 * 3) if connected, try to access Remote Config; if not, intent to ErrorActivity
 * 4) if URL from remote config fails to get JSONHome, normal flux but with catalog info
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
         * Here we try and get root, if that fails we check connectivity and the amount of times
         * this request has been attempted
         *
         * We can see if this is the first attempt by the value in the RemoteConfigLiveData object:
         * if null, there has not been a request to the remote config file which means it is the first
         * time the app tries to get the home resource
         */
        loadingViewModel.observeRootLiveData(this) {
            when (it) {
                is FetchSuccess<Root> -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(MAIN_ACTIVITY_ROOT_EXTRA, it.value)
                    this.startActivity(intent)
                }
                is FetchFailure<Root> -> { //unsuccessful request to jsonhome
                    if (loadingViewModel.getRemoteConfigLiveData() == null) { //first attempt at api request
                        if (!IonApplication.connectivityObservable.hasConnectivity()) { //connectivity check to see if that's why request failed
                            startActivity(
                                Intent(this, ErrorActivity::class.java)
                                    .putExtra(
                                        ERROR_ACTIVITY_EXCEPTION_EXTRA,
                                        resources.getString(R.string.label_no_connectivity_loading_error)
                                    )
                            )
                        } else {
                            loadingViewModel.getRemoteConfig()
                        }
                    } else { // all attempts failed, using catalog info
                        val intent = Intent(this, CatalogMainActivity::class.java)
                        this.startActivity(intent)
                    }
                }
            }
        }

        /**
         * If the RemoteConfig request is successful, we try to get the JsonHome resource again
         *
         * if it fails, GithHub is probably down so there's no point in trying to use teh catalog info, so we
         * just open the ISEL page.
         *
         * We don't need to check for connectivity in this case because the connectivity check is done
         * before we reach this stage of the fallback plan
         */
        loadingViewModel.observeRemoteConfigLiveData(this) {
            when (it) {
                is FetchSuccess<RemoteConfig> -> loadingViewModel.getJsonHome(URI(it.value.api_link))
                is FetchFailure<RemoteConfig> -> {
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

    /**
     * if the remote config link is the same as the link in the app,
    it means the API is down and for now we redirect to the ISEL website
     */
    private fun openISELPage() {
        val iselURL = "https://www.isel.pt"
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(iselURL)))
    }
}
