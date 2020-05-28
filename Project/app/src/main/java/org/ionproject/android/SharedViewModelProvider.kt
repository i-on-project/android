package org.ionproject.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class SharedViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SharedViewModel::class.java -> SharedViewModel(IonApplication.rootRepository)
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }

}