package org.ionproject.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.Root
import org.ionproject.android.offline.models.*
import java.net.URI

/**
 * This view model is used to shared information between fragments and the main activity,
 * the other approach is to use arguments, but that approach is not recommended by the android
 * docs if you are sharing objects.
 */
class SharedViewModel : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
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
     * [Root] used check for the existence of resources
     */
    var root: Root? = null

    /**
     * [courseDetailsUri] is used to pass the uri from [org.ionproject.android.courses.CoursesFragment]
    to [org.ionproject.android.course_details.CourseDetailsFragment] which is required to obtain the CourseDetails.
     */
    var courseDetailsUri: URI? = null

    /**
     * [classSectionUri] is used to pass the uri from [org.ionproject.android.course_details.CourseDetailsFragment]
    and [org.ionproject.android.favorites.FavoritesFragment] to [org.ionproject.android.class_section.ClassSectionFragment]
    which is required to obtain the ClassSection.
     */
    var classSectionUri: URI? = null

    /**
     * [programmeDetailsUri] is used to pass the uri from [org.ionproject.android.programmes.ProgrammesFragment]
    to [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment] which is required to obtain the ProgrammeDetails.
     */
    var programmeDetailsUri: URI? = null

    /**
     * programme is used to pass data from [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment]
    to [org.ionproject.android.courses.CoursesFragment]
     */
    var programmeOfferSummaries: List<ProgrammeOfferSummary> = emptyList()
    var curricularTerm: Int = 0

    //-------Catalog Variables--------
    /**
     * The programme that the user chose from the list in the ProgrammesFragment (curso)
     */
    var selectedCatalogProgramme: CatalogProgramme? = null

    /**
     * The Term the user chose from the list of terms of the selected Programme (semestre)
     */
    var selectedCatalogProgrammeTerm: CatalogTerm? = null

    var parsedExamSchedule: ExamSchedule? = null

    var parsedTimeTable: Timetable? = null

    /**
    Name of the chosen class to the show the respective lectures in class section fragment (turma)
     */
    var selectedClass: String? = null

    /**
     * ACR of the selected course to help filter the classes and the events in the fragments
     */
    var selectedCourse: String? = null
}
