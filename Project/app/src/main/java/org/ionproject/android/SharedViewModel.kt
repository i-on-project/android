package org.ionproject.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.CourseSummary

class SharedViewModel : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
    private val searchText = MutableLiveData<String>()
    val queryText: String?
        get() = searchText.value

    fun observeQueryText(lifecycleOwner: LifecycleOwner, onUpdate: (String) -> Unit) {
        searchText.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    fun setQueryText(searchQuery: String) {
        searchText.postValue(searchQuery)
    }

    // Shared course summary
    lateinit var courseSummary: CourseSummary

    // Shared class summary
    lateinit var classSummary: ClassSummary

}
