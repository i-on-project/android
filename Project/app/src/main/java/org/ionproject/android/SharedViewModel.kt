package org.ionproject.android

import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.CourseSummary

/**
 * This view model is used to shared information between fragments and the main activity,
 * the other approach is to use arguments, but that approach is not recommended by the android
 * docs if you are sharing objects.
 */
class SharedViewModel : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
    lateinit var searchText: String

    // Shared course summary between course
    lateinit var courseSummary: CourseSummary

    // Shared class summary between class list, favorites and class section
    lateinit var classSummary: ClassSummary

}
