package org.ionproject.android.error

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class ErrorViewModel: ViewModel() {

    // jus trying to make this work, will be updated in the future

    companion object {
        private val worker: Executor = Executors.newSingleThreadExecutor()
    }

    private val remoteConfigURL = "https://raw.githubusercontent.com/Jtoliveira/test/main/Remote_Config.json"

    private val remoteConfigLiveData = MutableLiveData<String>()

    fun makeRequest(){

        val okHttpClient = OkHttpClient()

        worker.execute{
            remoteConfigLiveData.postValue(parseResponse(okHttpClient.newCall(createRequest(remoteConfigURL)).execute()))
        }
    }

    private fun createRequest(url: String) : Request =
        Request.Builder().url(url).build()

    private fun parseResponse(response: Response): String {

        return jacksonObjectMapper()
            .readValue<RemoteConfig>(response.body()?.string()!!)
            .api_link

    }

    fun observeErrorLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (String?) -> Unit) {
        remoteConfigLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}