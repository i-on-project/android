package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication
import java.lang.IllegalArgumentException

class CalendarViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CalendarViewModel::class.java -> CalendarViewModel(
                IonApplication.db.FavoriteDao(),
                IonApplication.calendarTermRepository,
                IonApplication.eventsRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}