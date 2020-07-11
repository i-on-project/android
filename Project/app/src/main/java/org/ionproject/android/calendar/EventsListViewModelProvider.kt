package org.ionproject.android.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EventsListViewModelProvider : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        when (modelClass) {
            EventsListViewModel::class.java -> EventsListViewModel()
            else -> throw IllegalArgumentException("Class $modelClass not supported by this provider")
        } as T
}