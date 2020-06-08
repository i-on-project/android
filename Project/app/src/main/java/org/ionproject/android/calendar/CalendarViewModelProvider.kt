package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class CalendarViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CalendarViewModel::class.java -> CalendarViewModel(
                IonApplication.favoritesRepository,
                IonApplication.calendarTermRepository,
                IonApplication.eventsRepository,
                IonApplication.classesRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}