package edu.isel.ion.android.class_section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.isel.ion.android.common.IonApplication
import java.lang.IllegalArgumentException

class ClassSectionViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass){
            ClassSectionViewModel::class.java -> ClassSectionViewModel(
                IonApplication.classSectionRepository
            )
            else -> throw IllegalArgumentException("Class ${modelClass} not supported by this provider")
        } as T
    }

}