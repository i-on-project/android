package org.ionproject.android

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.runBlocking
import org.ionproject.android.common.IonApplication
import org.ionproject.android.loading.LoadingActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class WebAPITests {


    @Test
    fun shouldGetRootResource() {
        val root = runBlocking {
            IonApplication.rootRepository.getJsonHome()
        }


    }

}
