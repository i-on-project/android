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
    private val favoritesLiveData = liveData {
        emitSource(favoritesRepository.getFavoritesFromTerm(CalendarTerm("1920v")))
    }
    fun getFavoritesFromCalendarTerm(calendarTerm: CalendarTerm) {
        /*viewModelScope.launch {
            val favorites = favoritesRepository.getFavoritesFromTerm(calendarTerm)
        }*/
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
    val calendarTerms : List<CalendarTerm> get() = calendarTermsLiveData.value ?: emptyList()
    fun observeCalendarTerms(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<CalendarTerm>) -> Unit
    ) {
        calendarTermsLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun deleteFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoritesRepository.removeFavorite(favorite)
        }
    }


}