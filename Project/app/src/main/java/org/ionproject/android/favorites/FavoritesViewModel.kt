package org.ionproject.android.favorites

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.Favorite
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.FavoriteRepository

class FavoritesViewModel(
    private val favoritesRepository: FavoriteRepository,
    private val calendarTermRepository: CalendarTermRepository
) : ViewModel() {

    /**
     * Favorites Section
     */
    private val favoritesLiveData = MediatorLiveData<List<Favorite>>()
    private var favoriteLiveDataSrc: LiveData<List<Favorite>>? = null

    /**
     * Updates the MediatorLiveData source according to the
     * calendarTerm, this way when the user selects a diferent
     * calendar term the favorites list is updated
     */
    fun getFavoritesFromCalendarTerm(calendarTerm: CalendarTerm) {
        //Removing current MediatorLiveDataSource if its not null
        favoriteLiveDataSrc?.let {
            favoritesLiveData.removeSource(it)
        }
        //Updating current data source
        favoriteLiveDataSrc = favoritesRepository.getFavoritesFromTerm(calendarTerm)
        favoriteLiveDataSrc?.let {
            favoritesLiveData.addSource(it) {
                favoritesLiveData.value = it
            }
        }
    }

    val favorites: List<Favorite> get() = favoritesLiveData.value ?: emptyList()
    fun observeFavorites(lifecycleOwner: LifecycleOwner, onUpdate: (List<Favorite>) -> Unit) {
        favoritesLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    /**
     * Calendar terms Section
     */
    private val calendarTermsLiveData = liveData {
        val calendarTerms = calendarTermRepository.getAllCalendarTerm()
        emit(calendarTerms)
    }
    val calendarTerms: List<CalendarTerm> get() = calendarTermsLiveData.value ?: emptyList()
    fun observeCalendarTerms(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<CalendarTerm>) -> Unit
    ) {
        calendarTermsLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    /**
     * Removes favorite from local database
     * @param favorite is the favorite to remove
     */
    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(favorite)
        }
    }

    /**
     * Adds favorite to local database
     * @param favorite is the favorite to add
     */
    fun addFavorite(favorite: ClassSummary) {
        viewModelScope.launch {
            favoritesRepository.addFavorite(favorite)
        }
    }
}