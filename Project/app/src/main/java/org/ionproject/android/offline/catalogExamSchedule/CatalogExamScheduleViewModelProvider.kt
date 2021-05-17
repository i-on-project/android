package org.ionproject.android.offline.catalogExamSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CatalogExamScheduleViewModelProvider : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val model = when (modelClass) {
            CatalogExamScheduleViewModel::class.java -> CatalogExamScheduleViewModel(
                IonApplication.catalogRepository
            )
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
        return model as T
    }
}