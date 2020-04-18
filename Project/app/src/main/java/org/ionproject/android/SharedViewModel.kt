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

    /**
     * searchText is used to pass data from [MainActivity.topBar]
    to [org.ionproject.android.search.SearchResultsFragment]
     */
    lateinit var searchText: String

    /**
     * courseSummary is used to pass data from [org.ionproject.android.courses.CoursesFragment]
    to [org.ionproject.android.course_details.CourseDetailsFragment]
     */
    lateinit var courseSummary: CourseSummary

    /**
     * classSummary is used to pass data from [org.ionproject.android.course_details.CourseDetailsFragment]
    and [org.ionproject.android.favorites.FavoritesFragment] to [org.ionproject.android.class_section.ClassSectionFragment]
     */
    lateinit var classSummary: ClassSummary

}
