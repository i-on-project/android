package org.ionproject.android.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

/**
 *  Type used to instanciate the HomeViewModel,
 *  ensures that the ViewModel survives even after
 *  screen rotation
 */
class HomeViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            HomeViewModel::class.java -> HomeViewModel(
                IonApplication.suggestionsMockRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }

}