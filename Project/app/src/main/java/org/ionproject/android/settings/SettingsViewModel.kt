package org.ionproject.android.settings

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.repositories.CalendarTermRepository
import java.net.URI

class SettingsViewModel(
    private val calendarTermRepository: CalendarTermRepository
) : ViewModel() {

    // For simplification purposes, we will only allow to present schedule from the last 4 calendar terms
    private val numberOfCalendarTerms = 4

    /**
     * Calendar terms Section
     */
    private val calendarTermsLiveData = MutableLiveData<List<CalendarTerm>>()

    fun getAllPossibleScheduleCalendarTerms(calendarTermsUri: URI) {
        viewModelScope.launch {
            val calendarTerms = calendarTermRepository.getAllCalendarTerm(calendarTermsUri)
            calendarTermsLiveData.postValue(calendarTerms.subList(0, numberOfCalendarTerms))
        }
    }

    fun observeCalendarTerms(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<CalendarTerm>) -> Unit
    ) {
        calendarTermsLiveData.observe(lifecycleOwner, Observer {
            onUpdate(it)
        })
    }

    fun getCalendarTermIndex(currScheduleTerm: String): Int? {
        val calendarTerms = calendarTermsLiveData.value
        return calendarTerms?.indexOfFirst {
            it.name == currScheduleTerm
        }
    }

}