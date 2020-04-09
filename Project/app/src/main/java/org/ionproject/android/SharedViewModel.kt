package org.ionproject.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.CourseSummary

class SharedViewModel : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
    lateinit var searchText: String

    // Shared course summary
    lateinit var courseSummary: CourseSummary

    // Shared class summary
    lateinit var classSummary: ClassSummary

}
