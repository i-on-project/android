package org.ionproject.android.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class ScheduleViewModelProvider : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            ScheduleViewModel::class.java -> ScheduleViewModel(
                IonApplication.sharedPreferences,
                IonApplication.favoritesRepository,
                IonApplication.calendarTermRepository,
                IonApplication.classesRepository,
                IonApplication.eventsRepository
            )
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
    }

}