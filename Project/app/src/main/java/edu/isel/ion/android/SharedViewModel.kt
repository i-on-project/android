package edu.isel.ion.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import edu.isel.ion.android.common.model.CourseSummary

class SharedViewModel : ViewModel() {

    //Search text used to pass data from search bar to searchResultFragment
    val searchText = MutableLiveData<String>()

    fun observeSearchText(lifecycleOwner: LifecycleOwner, onUpdate : (String) -> Unit) {
        searchText.observe(lifecycleOwner, Observer(onUpdate))
    }

    val courseSummaryLiveData = MutableLiveData<CourseSummary>()

    val courseSummary : CourseSummary get() = courseSummaryLiveData.value!!

    fun observeCourseSummary(lifecycleOwner: LifecycleOwner, onUpdate : (CourseSummary) -> Unit) {
        courseSummaryLiveData.observe(lifecycleOwner, Observer(onUpdate))
    }

}