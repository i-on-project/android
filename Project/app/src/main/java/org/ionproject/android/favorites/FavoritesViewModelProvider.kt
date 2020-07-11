package org.ionproject.android.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class FavoritesViewModelProvider : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            FavoritesViewModel::class.java -> FavoritesViewModel(
                IonApplication.favoritesRepository,
                IonApplication.calendarTermRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}