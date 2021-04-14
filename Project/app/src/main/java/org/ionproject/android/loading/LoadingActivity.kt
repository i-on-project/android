package org.ionproject.android.loading

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_loading.*
import org.ionproject.android.ExceptionHandlingActivity
import org.ionproject.android.R
import org.ionproject.android.common.addGradientBackground
import org.ionproject.android.error.ErrorActivity
import org.ionproject.android.main.MAIN_ACTIVITY_ROOT_EXTRA
import org.ionproject.android.main.MainActivity

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
                intent.putExtra(MAIN_ACTIVITY_ROOT_EXTRA, it)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, ErrorActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}
