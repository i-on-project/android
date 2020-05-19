package org.ionproject.android.schedule

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.repositories.CalendarTermRepository
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository

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
        onUpdate: (Array<MutableList<Lecture>>) -> Unit
    ) {
        lecturesLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    private fun sortLecturesByDay(lectures: List<Lecture>): Array<MutableList<Lecture>> {
        val lecturesByDayOfWeek2 = arrayOf<MutableList<Lecture>>(
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf(),
            mutableListOf()
        )

        lectures.forEach {
            when (it.day) {
                "MO" -> lecturesByDayOfWeek2[0].add(it)
                "TU" -> lecturesByDayOfWeek2[1].add(it)
                "WE" -> lecturesByDayOfWeek2[2].add(it)
                "TH" -> lecturesByDayOfWeek2[3].add(it)
                "FR" -> lecturesByDayOfWeek2[4].add(it)
                "SA" -> lecturesByDayOfWeek2[5].add(it)
                "SU" -> lecturesByDayOfWeek2[6].add(it)
            }
        }
        return lecturesByDayOfWeek2
    }

    // Each index represents a different day of week, 0 - monday, 1 - tuesday, ...
    val lecturesByDayOfWeek: Array<MutableList<Lecture>>
        get() {
            val value = lecturesLiveData.value
            if (value == null)
                return arrayOf(
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf(),
                    mutableListOf()
                )
            else {
                return value
            }
        }
}