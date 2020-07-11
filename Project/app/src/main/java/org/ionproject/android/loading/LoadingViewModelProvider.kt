package org.ionproject.android.loading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class LoadingViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoadingViewModel::class.java -> LoadingViewModel(IonApplication.rootRepository)
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }

}