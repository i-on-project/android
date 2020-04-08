package org.ionproject.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SharedViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            _root_ide_package_.org.ionproject.android.SharedViewModel::class.java -> _root_ide_package_.org.ionproject.android.SharedViewModel()
            else -> IllegalArgumentException("Class ${modelClass} is not valid for this provider")
        } as T
    }

}