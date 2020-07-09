package org.ionproject.android.settings

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.SharedPreferences
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.repositories.CalendarTermRepository
import java.net.URI

class SettingsViewModel(
    private val sharedPreferences: SharedPreferences,
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
            val size =
                if (calendarTerms.size < numberOfCalendarTerms) calendarTerms.size else numberOfCalendarTerms
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

    // Returns the index of the Calendar Term whose name should be equal to the selected calendar term
    fun getCalendarTermIndex(selectedCalendarTerm: String): Int? {
        val calendarTerms = calendarTermsLiveData.value
        return calendarTerms?.indexOfFirst {
            it.name == selectedCalendarTerm
        }
    }

    /**
     * This should get the selected calendar term, if available, on shared preferences file
     *
     * @param key The key in order to get the corresponding value
     * @return The value found on shared preferences file or [null] if not available
     */
    fun getSelectedCalendarTerm(key: String) = sharedPreferences.getSelectedCalendarTerm(key)

    /**
     * This should write to the shared preferences the current selected calendar term
     * @param pair The key-value pair to write to shared preferences
     */
    fun setSelectedCalendarTerm(pair: Pair<String, String>) =
        sharedPreferences.writeToSharePref(pair)

}