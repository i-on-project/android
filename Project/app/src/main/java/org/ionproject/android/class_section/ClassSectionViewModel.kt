package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Favorite
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.FavoriteRepository

class ClassSectionViewModel(
    private val classSectionRepository: ClassesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val classSectionLiveData = MutableLiveData<ClassSection>()
    private val classSection: ClassSection? get() = classSectionLiveData.value

    fun getClassSectionDetails(classSummary: ClassSummary) {
        viewModelScope.launch {
            val classSectionData =
                classSectionRepository.getClassSection(classSummary)
            classSectionLiveData.postValue(classSectionData)
        }
    }

    fun observeForClassSectionData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (ClassSection) -> Unit
    ) {
        classSectionLiveData.observe(lifecycleOwner, Observer(onUpdate))
    }

    /**
     * Adds the current classSection to the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun addClassSectionToFavorites(): Boolean {
        val thisClassSection = classSection
        if (thisClassSection != null) {
            val favorite = Favorite(
                thisClassSection.calendarTerm,
                thisClassSection.id,
                thisClassSection.course
            )
            viewModelScope.launch {
                classSectionRepository.addClassSectionToDb(thisClassSection)
                favoriteRepository.addFavorite(favorite)
            }
            return true
        }
        return false
    }

    /**
     * Removes the current classSection from the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun removeClassSectionFromFavorites(): Boolean {
        val thisClassSection = classSection
        if (thisClassSection != null) {
            val favorite = Favorite(
                thisClassSection.calendarTerm,
                thisClassSection.id,
                thisClassSection.course
            )
            viewModelScope.launch {
                favoriteRepository.removeFavorite(favorite)
                classSectionRepository.removeClassSectionFromDb(thisClassSection)
            }
            return true
        }
        return false
    }

    /**
     * Checks if this class section is a favorite
     */
    fun isThisClassSectionFavorite(onUpdate: (Boolean) -> Unit): Boolean {
        val thisClassSection = classSection
        if (thisClassSection != null) {
            val favorite = Favorite(
                thisClassSection.calendarTerm,
                thisClassSection.id,
                thisClassSection.course
            )
            viewModelScope.launch {
                onUpdate(
                    favoriteRepository.favoriteExists(favorite)
                )
            }
            return true
        }
        return false
    }
}
