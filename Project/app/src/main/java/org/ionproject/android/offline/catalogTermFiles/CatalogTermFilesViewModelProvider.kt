package org.ionproject.android.offline.catalogTermFiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication
import org.ionproject.android.offline.catalogProgrammeDetails.CatalogProgrammeDetailsViewModel

class CatalogTermFilesViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            CatalogTermFilesViewModel::class.java -> CatalogTermFilesViewModel(
                IonApplication.catalogRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }
}