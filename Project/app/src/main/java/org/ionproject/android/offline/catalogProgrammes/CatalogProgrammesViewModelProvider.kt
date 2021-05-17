package org.ionproject.android.offline.catalogProgrammes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CatalogProgrammesViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            CatalogProgrammesViewModel::class.java -> CatalogProgrammesViewModel(
                IonApplication.catalogRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }
}