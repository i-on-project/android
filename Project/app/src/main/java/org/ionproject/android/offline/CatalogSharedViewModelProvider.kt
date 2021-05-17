package org.ionproject.android.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.SharedViewModel

class CatalogSharedViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CatalogSharedViewModel::class.java -> CatalogSharedViewModel()
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }

}