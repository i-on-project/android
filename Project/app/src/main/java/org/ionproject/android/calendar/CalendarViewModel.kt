package org.ionproject.android.calendar

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
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

    fun getFavoriteClassesFromCurrentTerm(
        lifecycleOwner: LifecycleOwner,
        onResult: (List<ClassSummary>) -> Unit
    ) {
        viewModelScope.launch {
            val currentTerm = calendarTermRepository.getAllCalendarTerm().first()
            val favorites = favoriteRepository.getFavoritesFromTerm(currentTerm)
            favorites.observe(lifecycleOwner, Observer { onResult(it) })
        }
    }

    fun getEvents(
        classes: List<ClassSummary>,
        onResult: (List<Events>) -> Unit
    ) {
        viewModelScope.launch {
            val events = classes.mapNotNull {
                val classSection = classesRepository.getClassSection(it)
                val uri = classSection.calendarURI
                if (uri != null) eventsRepository.getEvents(uri)
                else null
            }
            onResult(events)
        }
    }

}

