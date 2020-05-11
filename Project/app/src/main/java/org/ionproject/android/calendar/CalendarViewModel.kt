package org.ionproject.android.calendar

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.TAG
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository

class CalendarViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val calendarTermRepository: CalendarTermRepository,
    private val eventsRepository: EventsRepository,
    private val classesRepository: ClassesRepository
) : ViewModel() {

    private val favoritesLiveData = MutableLiveData<List<ClassSummary>>()
    private val eventsLiveData = MutableLiveData<List<Events>>()

    fun getFavoriteClassesFromCurrentTerm() {
        viewModelScope.launch {
            val currentTerm = calendarTermRepository.getAllCalendarTerm().first()
            val favorites = favoriteRepository.getFavoritesFromTerm(currentTerm)
            // TODO: Fix this bug - Receiving null from FavoritesRepository
            favoritesLiveData.postValue(favorites.value)
        }
    }

    fun getEvents(classes: List<ClassSummary>) {
        viewModelScope.launch {
            val events = classes.mapNotNull {
                val classSection = classesRepository.getClassSection(it)
                val uri = classSection.calendarURI
                if (uri != null) eventsRepository.getEvents(uri)
                else null
            }
            eventsLiveData.postValue(events)
        }
    }

    fun observeFavorites(lifecycleOwner: LifecycleOwner, onResult: (List<ClassSummary>) -> Unit) {
        favoritesLiveData.observe(lifecycleOwner, Observer {
            onResult(it ?: emptyList())
        })
    }

    fun observeEvents(lifecycleOwner: LifecycleOwner, onResult: (List<Events>) -> Unit) {
        eventsLiveData.observe(lifecycleOwner, Observer {
            onResult(it)
        })
    }

}

