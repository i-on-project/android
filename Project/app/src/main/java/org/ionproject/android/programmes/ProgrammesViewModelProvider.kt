package org.ionproject.android.programmes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class ProgrammesViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            ProgrammesViewModel::class.java -> ProgrammesViewModel(
                IonApplication.programmesRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }

}