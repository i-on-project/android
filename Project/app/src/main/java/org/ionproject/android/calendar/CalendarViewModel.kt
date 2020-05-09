package org.ionproject.android.calendar

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.db.FavoriteDao
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.EventsRepository

class CalendarViewModel(
    private val favoriteDao: FavoriteDao,
    private val calendarTermRepository: CalendarTermRepository,
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val eventsLiveData = MutableLiveData<List<Lecture>>()

    fun getEventsFromCurrentTerm() {
        viewModelScope.launch {
            val currentTerm = calendarTermRepository.getAllCalendarTerm().first().name
            val favorites = favoriteDao.findFavoritesFromCalendarTerm(currentTerm)
            val events = favorites.value?.flatMap {
                eventsRepository.getLectures(it.detailsUri)
            }
            eventsLiveData.postValue(events)
        }
    }

    fun observeEvents(lifeCycle: LifecycleOwner, onResult: (List<Lecture>) -> Unit) {
        eventsLiveData.observe(lifeCycle, Observer {
            onResult(it)
        })
    }
}