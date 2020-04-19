package org.ionproject.android.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CoursesViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CoursesViewModel::class.java -> CoursesViewModel(
                IonApplication.coursesRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }
}