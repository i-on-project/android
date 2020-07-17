package org.ionproject.android.schedule

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.listOf
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.WeekDay
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository
import org.ionproject.android.settings.Preferences
import java.net.URI

const val NUMBER_OF_WEEK_DAYS = 7

class ScheduleViewModel(
    private val preferences: Preferences,
    private val favoriteRepository: FavoriteRepository,
    private val calendarTermRepository: CalendarTermRepository,
    private val classesRepository: ClassesRepository,
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val lecturesLiveData = MutableLiveData<List<MutableList<Lecture>>>()

    /**
     * Get selected calendar term from app settings
     * Get all favorites from that calendar term
     * Get the details of all favorites
     * Get all lectures from those favorites
     * Sort all lectures according to the days of week
     * post to live data to notify observers
     */
    fun getLecturesFromSelectedCalendarTerm(scheduleCalendarTerm: String) = viewModelScope.launch {
        val calendarTerm = CalendarTerm.fromString(scheduleCalendarTerm)
        getLectures(calendarTerm)
    }

    /**
     * In case there is no selected calendar term from App Settings then
     * get lectures from current calendar term
     */
    fun getLecturesFromCurrentCalendar(calendarTermsUri: URI) =
        viewModelScope.launch {
            val calendarTerm = calendarTermRepository.getMostRecentCalendarTerm(calendarTermsUri)
            getLectures(calendarTerm)
        }

    fun observerLecturesLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<MutableList<Lecture>>) -> Unit
    ) {
        lecturesLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    private fun sortLecturesByDay(lectures: List<Lecture>): List<MutableList<Lecture>> {
        val listOfLectureList = listOf(NUMBER_OF_WEEK_DAYS) { mutableListOf<Lecture>() }

        lectures.forEach {
            when (it.weekDay) {
                WeekDay.MONDAY -> listOfLectureList[0].add(it)
                WeekDay.TUESDAY -> listOfLectureList[1].add(it)
                WeekDay.WEDNESDAY -> listOfLectureList[2].add(it)
                WeekDay.THURSDAY -> listOfLectureList[3].add(it)
                WeekDay.FRIDAY -> listOfLectureList[4].add(it)
                WeekDay.SATURDAY -> listOfLectureList[5].add(it)
                WeekDay.SUNDAY -> listOfLectureList[6].add(it)
            }
        }
        return listOfLectureList
    }

    private suspend fun getLectures(calendarTerm: CalendarTerm) {
        val favorites = favoriteRepository.suspendGetFavoritesFromTerm(calendarTerm)
        val classSections = favorites.map {
            classesRepository.getClassSection(it.selfURI)
        }
        val lectures = classSections.flatMap {
            var toRet = emptyList<Lecture>()
            if (it?.calendarURI != null) {
                val events = eventsRepository.getEvents(it.calendarURI)
                if (events != null)
                    toRet = events.lectures
            }
            toRet
        }
        lecturesLiveData.postValue(sortLecturesByDay(lectures))
    }

    /**
     * This should get the selected calendar term, if available, on shared preferences file
     *
     * @param key The key in order to get the corresponding value
     * @return The value found on shared preferences file or [null] if not available
     */
    fun getSelectedCalendarTerm() = preferences.getCalendarTerm()

    // Each index represents a different day of week, 0 - monday, 1 - tuesday, ...
    val lecturesByDayOfWeek: List<MutableList<Lecture>>
        get() = lecturesLiveData.value ?: listOf(NUMBER_OF_WEEK_DAYS) { mutableListOf<Lecture>() }

}
