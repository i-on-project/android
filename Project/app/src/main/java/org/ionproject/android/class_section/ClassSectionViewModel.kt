package org.ionproject.android.class_section

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.icalendar.ExamSummary
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository
import java.net.URI

class ClassSectionViewModel(
    private val eventsRepository: EventsRepository,
    private val classSectionRepository: ClassesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val examsLiveData = MutableLiveData<List<ExamSummary>>()

    val exams: List<ExamSummary>
        get() = examsLiveData.value ?: emptyList()

    /**
     * Current class section obtained from Web API
     */
    private lateinit var currClassSection: ClassSection

    /**
     * Obtains a classSection from WebApi and updated the UI with the result
     */
    fun getClassSectionDetails(classSummary: ClassSummary, onResult: (ClassSection) -> Unit) {
        viewModelScope.launch {
            currClassSection =
                classSectionRepository.getClassSection(classSummary)
            onResult(currClassSection)
        }
    }

    /**
     * Adds the current classSection to the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun addClassToFavorites(classSummary: ClassSummary) {
        viewModelScope.launch {
            classSectionRepository.addClassSectionToDb(currClassSection)
            favoriteRepository.addFavorite(classSummary)
        }
    }

    /**
     * Removes the current classSection from the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun removeClassFromFavorites(classSummary: ClassSummary) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(classSummary)
            classSectionRepository.removeClassSectionFromDb(currClassSection)
        }
    }

    /**
     * Checks if this class section is a favorite
     */
    fun isThisClassFavorite(classSummary: ClassSummary, onUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            onUpdate(
                favoriteRepository.favoriteExists(classSummary)
            )
        }
    }

    fun getExams(uris: List<URI>, onResult:() -> Unit) {
        viewModelScope.launch {
            val exams: List<ExamSummary> = uris.map { eventsRepository.getExamFromCourse(it) }
            examsLiveData.postValue(exams)
            onResult()
        }
    }
}
