package org.ionproject.android.loading

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.Root
import org.ionproject.android.common.repositories.RootRepository

class LoadingViewModel(private val rootRepository: RootRepository) : ViewModel() {

    private val rootLiveData = MutableLiveData<Root?>()

    init {
        viewModelScope.launch {
            val root = rootRepository.getJsonHome()
            rootLiveData.postValue(root)
        }
    }

    fun observeRootLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (Root?) -> Unit) {
        rootLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }
}