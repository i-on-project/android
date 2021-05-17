package org.ionproject.android.offline.catalogProgrammeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CatalogProgrammeDetailsViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            CatalogProgrammeDetailsViewModel::class.java -> CatalogProgrammeDetailsViewModel(
                IonApplication.catalogRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }
}