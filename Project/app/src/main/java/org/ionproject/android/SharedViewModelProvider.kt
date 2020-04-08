package org.ionproject.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SharedViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SharedViewModel::class.java -> SharedViewModel()
            else -> IllegalArgumentException("Class ${modelClass} is not valid for this provider")
        } as T
    }

}