package org.ionproject.android.offline

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.ionproject.android.offline.models.CatalogCalendar
import org.ionproject.android.offline.models.CatalogProgramme
import org.ionproject.android.offline.models.ExamSchedule
import org.ionproject.android.offline.models.Timetable

class CatalogSharedViewModel : ViewModel() {

    /**
     * Search text used to pass data from search bar to Exam Fragment
      */
    private val searchTextLiveData = MutableLiveData<String>()

    /**
     * Updates SearchTextLiveData
     */
    fun setSearchText(query: String) {
        searchTextLiveData.postValue(query)
    }

    /**
     * Observes the live data and calls onUpdate when a change occurs
     */
    fun observeSearchText(lifecycleOwner: LifecycleOwner, onUpdate: (String) -> Unit) {
        searchTextLiveData.observe(lifecycleOwner, Observer {
            onUpdate(it)
        })
    }

    /**
     * The programme that the user chose from the list in the ProgrammesFragment (curso)
     */
    var selectedCatalogProgramme: CatalogProgramme? = null

    /**
     * The Term the user chose from the list of terms of the selected Programme (semestre)
     */
    var selectedCatalogProgrammeTerm: String = ""

    var selectedYear: String = ""
}