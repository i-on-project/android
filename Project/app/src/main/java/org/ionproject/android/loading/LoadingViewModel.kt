package org.ionproject.android.loading

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository
import java.net.URI

// This uri has to be hardcoded there is no other way
private val ROOT_URI_V0 = URI("/")

sealed class FetchResult<out T>
data class FetchFailure<T>(val throwable: Throwable? = null) : FetchResult<T>()
data class FetchSuccess<T>(val value: T) : FetchResult<T>()

class LoadingViewModel(
    private val rootRepository: RootRepository,
    private val remoteConfigRepository: RemoteConfigRepository) : ViewModel() {

    private val rootLiveData = MutableLiveData<FetchResult<Root>>()
    private val remoteConfigLiveData = MutableLiveData<FetchResult<RemoteConfig>>()

    init {
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
            val result = try {
                val root = rootRepository.getJsonHome(uri)
                if (root != null) FetchSuccess(root) else FetchFailure<Root>()
            } catch(e: Exception) {
                FetchFailure<Root>(e)
            }

            rootLiveData.postValue(result)
        }
    }

    fun getRemoteConfig(){
        viewModelScope.launch {
            val result = try {
                val remoteConfig = remoteConfigRepository.getRemoteConfig()
                if (remoteConfig != null) FetchSuccess(remoteConfig) else FetchFailure<RemoteConfig>()
            } catch(e: Exception) {
                FetchFailure<RemoteConfig>(e)
            }

            remoteConfigLiveData.postValue(result)
        }
    }

    fun observeRootLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<Root>) -> Unit) {
        rootLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun observeRemoteConfigLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<RemoteConfig>) -> Unit) {
        remoteConfigLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun getRemoteConfigLiveData() = remoteConfigLiveData.value
}