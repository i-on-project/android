package org.ionproject.android.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CatalogMainActivityViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CatalogMainActivityViewModel::class.java -> CatalogMainActivityViewModel(IonApplication.rootRepository)
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }

}