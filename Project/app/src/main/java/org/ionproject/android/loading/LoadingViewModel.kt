package org.ionproject.android.loading

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchResult
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository
import java.net.URI

class LoadingViewModel(
    private val rootRepository: RootRepository,
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    private val rootLiveData = MutableLiveData<FetchResult<Root>>()
    private val remoteConfigLiveData = MutableLiveData<FetchResult<RemoteConfig>>()

    init {
        getJsonHome(URI(remoteConfigRepository.preferences.getWebApiHost()))
    }

    /**
    Retrofit note: since retrofit 2 uses okhttp's HttpUrl, we don't need to change the base url of
    the service in endpoints where the method annotation doesn't specify a url. it detects if the
    @URL parameter doesn't match with the base url and sorts everything out by itself
     */
    fun getJsonHome(uri: URI) {
        viewModelScope.launch {
            val result = try {
                val root = rootRepository.getJsonHome(uri)
                if (root != null) FetchSuccess(root) else FetchFailure<Root>()
            } catch (e: Exception) {
                FetchFailure<Root>(e)
            }

            rootLiveData.postValue(result)
        }
    }

    fun getRemoteConfig() {
        viewModelScope.launch {
            val result = try {
                val remoteConfig = remoteConfigRepository.getRemoteConfig()
                if (remoteConfig?.api_link != null) FetchSuccess(remoteConfig) else FetchFailure<RemoteConfig>()
            } catch (e: Exception) {
                FetchFailure<RemoteConfig>(e)
            }

            remoteConfigLiveData.postValue(result)
        }
    }

    fun observeRootLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<Root>) -> Unit) {
        rootLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun observeRemoteConfigLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (FetchResult<RemoteConfig>) -> Unit
    ) {
        remoteConfigLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun getRemoteConfigLiveData() = remoteConfigLiveData.value
}