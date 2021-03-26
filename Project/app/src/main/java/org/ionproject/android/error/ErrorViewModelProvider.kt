package org.ionproject.android.error

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication
import org.ionproject.android.loading.LoadingViewModel

class ErrorViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ErrorViewModel::class.java -> ErrorViewModel()
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }

}