package org.ionproject.android.offline.catalogTimetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.Timetable

class CatalogTimetableViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    /**
     * Get the TimeTable from the [catalogTermFilesLiveData] object
     */
    fun getCatalogTimetable(programme: String, term: String, onResult: (Timetable) -> Unit) {
        viewModelScope.launch {

            catalogRepository.getFileFromGithub(
                programme,
                term,
                Timetable::class.java
            ).let {
                onResult(it)
            }
        }
    }
}