package org.ionproject.android.settings

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.Preferences
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.repositories.CalendarTermRepository
import java.net.URI

// For simplification purposes, we will only allow to present schedule from the last 4 calendar terms
private const val NUMBER_OF_CALENDAR_TERMS = 4

class SettingsViewModel(
    private val preferences: Preferences,
    private val calendarTermRepository: CalendarTermRepository
) : ViewModel() {

    /**
     * Calendar terms Section
     */
    private val calendarTermsLiveData = MutableLiveData<List<CalendarTerm>>()

    fun getAllCalendarTerms(calendarTermsUri: URI) {
        viewModelScope.launch {
            val calendarTerms = calendarTermRepository.getAllCalendarTerm(calendarTermsUri)
            val size =
                if (calendarTerms.size < NUMBER_OF_CALENDAR_TERMS) calendarTerms.size else NUMBER_OF_CALENDAR_TERMS
            calendarTermsLiveData.postValue(calendarTerms.subList(0, size))
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

    /**
     * This should get the selected calendar term, if available, on shared preferences file
     *
     * @param key The key in order to get the corresponding value
     * @return The value found on shared preferences file or [null] if not available
     */
    fun getSelectedCalendarTerm() = preferences.getCalendarTerm()

    /**
     * This should write to the shared preferences the current selected calendar term
     * @param calendarTerm The new calendar term to write to shared preferences
     */
    fun setSelectedCalendarTerm(calendarTerm: String) =
        preferences.saveCalendarTerm(calendarTerm)

}