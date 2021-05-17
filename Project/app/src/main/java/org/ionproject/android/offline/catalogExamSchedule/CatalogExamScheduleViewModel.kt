package org.ionproject.android.offline.catalogExamSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.ExamSchedule

class CatalogExamScheduleViewModel(private val catalogRepository: CatalogRepository): ViewModel() {

    /**
     * Get the Exam Schedule from the [catalogTermFilesLiveData] object
     */
    fun getCatalogExamSchedule(programme: String, term: String, onResult: (ExamSchedule) -> Unit) {
        viewModelScope.launch {

            catalogRepository.getFileFromGithub(
                programme,
                term,
                ExamSchedule::class.java
            ).let {
                onResult(it)
            }
        }
    }
}