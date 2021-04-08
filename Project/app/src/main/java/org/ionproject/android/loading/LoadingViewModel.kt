package org.ionproject.android.loading

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository
import java.net.URI

// This uri has to be hardcoded there is no other way
private val ROOT_URI_V0 = URI("/")

class LoadingViewModel(private val rootRepository: RootRepository, private val remoteConfigRepository: RemoteConfigRepository) : ViewModel() {

    private val rootLiveData = MutableLiveData<Root?>()

    private val remoteConfigLiveData = MutableLiveData<RemoteConfig?>()

    var fresh = false

    init {
        /*viewModelScope.launch {
            try{
                val root = rootRepository.getJsonHome()
                rootLiveData.postValue(root)
            }catch(e: Exception){
                Log.i("pls", "error")
                rootLiveData.postValue(null)
            }
        }*/
        getJsonHome(ROOT_URI_V0)
    }

    /*
    Added a try catch bc the automated Exception handling done by
    ExceptionHandlingActivity.kt wasn't allowing for the if in the observer to function

    Retrofit note: since retrofit 2 uses okhttp's HttpUrl, we don't need to change the base url of
    the service in endpoints where the method annotation doesn't specify a url. it detects if the
    @URL parameter doesn't match with the base url and sorts everything out by itself
     */
    fun getJsonHome(uri:URI){
        viewModelScope.launch {
            try{
                val root = rootRepository.getJsonHome(uri)
                rootLiveData.postValue(root)
            }catch(e: Exception){
                Log.d("API", "error")
                rootLiveData.postValue(null)
            }
        }
    }

    fun getRemoteConfig(){
        viewModelScope.launch {
            val remoteConfig = remoteConfigRepository.getRemoteConfig()
            remoteConfigLiveData.postValue(remoteConfig)
            fresh = true
        }
    }

    fun observeRootLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (Root?) -> Unit) {
        rootLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun observeRemoteConfigLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (RemoteConfig?) -> Unit) {
        remoteConfigLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}