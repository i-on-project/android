package org.ionproject.android.offline.catalogTermFiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogCalendar
import org.ionproject.android.offline.models.ExamSchedule
import org.ionproject.android.offline.models.Timetable

class CatalogTermFilesViewModel(private val catalogRepository: CatalogRepository): ViewModel() {

    fun getCatalogCalendar(year:String, onResult: (CatalogCalendar) -> Unit){
        viewModelScope.launch {
            catalogRepository.getCatalogCalendar(year).let {
                if (it != null) {
                    onResult(it)
                }
            }
        }
    }
}