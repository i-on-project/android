package org.ionproject.android.loading

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.MainActivity
import org.ionproject.android.R
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.IonApplication
import org.ionproject.android.error.ErrorActivity

const val ROOT_KEY = "m0192exe1gxe12x1"

class LoadingActivity : AppCompatActivity() {

    private val loadingViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, LoadingViewModelProvider())[LoadingViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        // If JsonHome contains all required resources then open main activity else app must be outdated
        loadingViewModel.observeRootLiveData(this) {
            if (it != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(ROOT_KEY, it)
                this.startActivity(intent)
            } else {
                val intent = Intent(this, ErrorActivity::class.java)
                this.startActivity(intent)
            }
        }
    }
}
