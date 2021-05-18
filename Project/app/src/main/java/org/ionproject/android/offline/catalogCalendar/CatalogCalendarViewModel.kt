package org.ionproject.android.offline.catalogCalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogCalendar

class CatalogCalendarViewModel(private val catalogRepository: CatalogRepository): ViewModel() {

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