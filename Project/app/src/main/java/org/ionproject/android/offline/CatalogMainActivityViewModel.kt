package org.ionproject.android.offline

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.FetchFailure
import org.ionproject.android.common.FetchResult
import org.ionproject.android.common.FetchSuccess
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository
import java.net.URI

class CatalogMainActivityViewModel(private val rootRepository: RootRepository): ViewModel(){

    private val rootLiveData = MutableLiveData<FetchResult<Root>>()

    /**
     * Same function from [LoadingViewModel] that requests the JSONHome
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

    fun observeRootLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (FetchResult<Root>) -> Unit) {
        rootLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}