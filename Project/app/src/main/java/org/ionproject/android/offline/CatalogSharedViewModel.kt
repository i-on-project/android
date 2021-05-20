package org.ionproject.android.offline

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.ionproject.android.offline.models.CatalogProgramme

class CatalogSharedViewModel : ViewModel() {

    /**
     * Search text used to pass data from search bar to Exam Fragment
     */
    private val searchTextLiveData = MutableLiveData<String>()

    /**
     * Query passed in the search action from the top bar .
     * We need this object so that the exam fragment
     * can know what the string was and filter the exam list accordingly
     */
    fun setSearchText(query: String) {
        searchTextLiveData.postValue(query)
    }

    /**
     * Observes the query so we can fire off the filter when the time is right
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

    /**
     * The year the user chose in the programme details fragment
     */
    var selectedYear: String = ""
}