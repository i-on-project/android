package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository

class CalendarViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val eventsRepository: EventsRepository,
    private val classesRepository: ClassesRepository
) : ViewModel() {

    /**
     * Gets all events available for all favorites chosen by the user
     */
    fun getAllEventsFromFavorites(
        onResult: (List<Events>) -> Unit
    ) {
        viewModelScope.launch {
            val events = favoriteRepository.getAllFavorites().mapNotNull {
                val classSection: ClassSection? = classesRepository.getClassSection(it.selfURI)
                if (classSection != null)
                    eventsRepository.getAllEventsFromClassSection(
                        classSection = classSection,
                        getEvents = { uri -> eventsRepository.getEvents(uri) },
                        getClassCollectionByUri = { classesUri ->
                            classesRepository.getClassCollectionByUri(classesUri)
                        })
                else
                    null
            }
            onResult(events)
        }
    }

}

