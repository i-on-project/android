package org.ionproject.android

import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.*

/**
 * This view model is used to shared information between fragments and the main activity,
 * the other approach is to use arguments, but that approach is not recommended by the android
 * docs if you are sharing objects.
 */
class SharedViewModel : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
    lateinit var searchText: String

    /**
     * [Root] used check for the existence of resources
     */
    lateinit var root: Root

    /**
     * programmeOffer is used to pass data from [org.ionproject.android.courses.CoursesFragment]
    to [org.ionproject.android.course_details.CourseDetailsFragment]
     */
    lateinit var programmeOffer: ProgrammeOffer

    /**
     * classSummary is used to pass data from [org.ionproject.android.course_details.CourseDetailsFragment]
    and [org.ionproject.android.favorites.FavoritesFragment] to [org.ionproject.android.class_section.ClassSectionFragment]
     */
    lateinit var classSummary: ClassSummary

    /**
     * programmeSummary is used to pass data from [org.ionproject.android.programmes.ProgrammesFragment]
    to [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment]
     */
    lateinit var programmeSummary: ProgrammeSummary

    /**
     * programme is used to pass data from [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment]
    to [org.ionproject.android.courses.CoursesFragment]
     */
    lateinit var programmeOfferSummaries: List<ProgrammeOfferSummary>
    var curricularTerm: Int = 0

}
