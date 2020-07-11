package org.ionproject.android.programmeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class ProgrammeDetailsViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ProgrammeDetailsViewModel::class.java -> ProgrammeDetailsViewModel(
                IonApplication.programmesRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }
}
