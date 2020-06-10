package org.ionproject.android.schedule

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.ionproject.android.common.listOf
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository

const val NUMBER_OF_WEEK_DAYS = 7

class ScheduleViewModel(
    private val favoriteRepository: FavoriteRepository,
    private val calendarTermRepository: CalendarTermRepository,
    private val classesRepository: ClassesRepository,
    private val eventsRepository: EventsRepository
) : ViewModel() {

    /**
     * Get most recent calendar term
     * Get all favorites from that calendar term
     * Get the details of all favorites
     * Get all lectures from those favorites
     * Sort all lectures according to the days of week
     * post to live data to notify observers
     */
    private val lecturesLiveData = liveData {
        val calendarTerm = calendarTermRepository.getMostRecentCalendarTerm()
        val favorites = favoriteRepository.suspendGetFavoritesFromTerm(calendarTerm)
        val classSections = favorites.map {
            classesRepository.getClassSection(it.toClassSummary())
        }
        val lectures = classSections.flatMap {
            eventsRepository.getLectures(it?.calendarURI)
        }
        emit(sortLecturesByDay(lectures))
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
            when (it.day) {
                "MO" -> listOfLectureList[0].add(it)
                "TU" -> listOfLectureList[1].add(it)
                "WE" -> listOfLectureList[2].add(it)
                "TH" -> listOfLectureList[3].add(it)
                "FR" -> listOfLectureList[4].add(it)
                "SA" -> listOfLectureList[5].add(it)
                "SU" -> listOfLectureList[6].add(it)
            }
        }
        return listOfLectureList
    }

    // Each index represents a different day of week, 0 - monday, 1 - tuesday, ...
    val lecturesByDayOfWeek: List<MutableList<Lecture>>
        get() = lecturesLiveData.value ?: listOf(NUMBER_OF_WEEK_DAYS) { mutableListOf<Lecture>() }


}