package edu.isel.ion.android.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
    Type used to instanciate the HomeViewModel,
    ensures that the ViewModel survives even after
    screen rotation
 */
class HomeViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       val model  = when(modelClass) {
            HomeViewModel::class.java -> HomeViewModel()
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }

}