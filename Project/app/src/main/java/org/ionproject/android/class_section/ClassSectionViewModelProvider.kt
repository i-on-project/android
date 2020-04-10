package org.ionproject.android.class_section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class ClassSectionViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ClassSectionViewModel::class.java -> ClassSectionViewModel(
                IonApplication.classesRepository,
                IonApplication.favoritesRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}