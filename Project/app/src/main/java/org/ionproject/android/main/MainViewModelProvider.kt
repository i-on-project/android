package org.ionproject.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

@Suppress("UNCHECKED_CAST")
class MainViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(IonApplication.connectivityObservable)
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }
}