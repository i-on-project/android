package edu.isel.ion.android.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CoursesViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            CoursesViewModel :: class.java -> CoursesViewModel()
            else -> throw IllegalArgumentException("Class ${modelClass} not supported by this provider")
        } as T
    }
}