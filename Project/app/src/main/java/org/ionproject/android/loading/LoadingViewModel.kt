package org.ionproject.android.loading

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository

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
        getJsonHome()
    }

    /*
    Added a try catch bc the automated Exception handling done by
    ExceptionHandlingActivity.kt wasn't allowing for the if in the observer to function
     */
    fun getJsonHome(){
        viewModelScope.launch {
            try{
                val root = rootRepository.getJsonHome()
                rootLiveData.postValue(root)
            }catch(e: Exception){
                Log.i("pls", "error")
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