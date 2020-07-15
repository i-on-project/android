package org.ionproject.android.calendar

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.model.Favorite
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository
import java.net.URI

class CalendarViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val calendarTermRepository: CalendarTermRepository,
    private val eventsRepository: EventsRepository,
    private val classesRepository: ClassesRepository
) : ViewModel() {

    /**
     * Gets all favorites found on local database that have been chosen by the user
     */
    fun getFavoriteClassesFromCurrentTerm(
        calendarTermsUri: URI,
        lifecycleOwner: LifecycleOwner,
        onResult: (List<Favorite>) -> Unit
    ) {
        viewModelScope.launch {
            val currentTerm = calendarTermRepository.getAllCalendarTerm(calendarTermsUri).first()
            val favorites = favoriteRepository.getFavoritesFromTerm(currentTerm)
            favorites.observe(lifecycleOwner, Observer { onResult(it) })
        }
    }

    /**
     * Gets all events available for all favorites chosen by the user
     */
    fun getAllEventsFromFavorites(
        favorites: List<Favorite>,
        onResult: (List<Events>) -> Unit
    ) {
        viewModelScope.launch {
            val events = favorites.mapNotNull {
                val classSection: ClassSection? = classesRepository.getClassSection(it.selfURI)
                if (classSection != null)
                    eventsRepository.getAllEventsFromClassSection(
                        classSection = classSection,
                        getEvents = { uri -> eventsRepository.getEvents(uri) },
                        getClassCollectionByUri = { classesUri ->
                            classesRepository.getClassCollectionByUri(classesUri)
                        })
                else
                    Events.NO_EVENTS
            }
            onResult(events)
        }
    }

}

