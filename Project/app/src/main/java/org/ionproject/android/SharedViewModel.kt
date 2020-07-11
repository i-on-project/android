package org.ionproject.android

import androidx.lifecycle.*
import org.ionproject.android.common.ConnectivityObserver
import org.ionproject.android.common.ObservableConnectivity
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.Root
import java.net.URI

/**
 * This view model is used to shared information between fragments and the main activity,
 * the other approach is to use arguments, but that approach is not recommended by the android
 * docs if you are sharing objects.
 */
class SharedViewModel(private val observableConnectivity: ObservableConnectivity) : ViewModel() {

    init {
        observableConnectivity.startObservingConnection(viewModelScope)
    }

    fun observeConnection(
        lifecycleOwner: LifecycleOwner,
        connectivityObserver: ConnectivityObserver
    ) =
        observableConnectivity.observe(lifecycleOwner, connectivityObserver)

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
    lateinit var root: Root

    /**
     * [courseDetailsUri] is used to pass the uri from [org.ionproject.android.courses.CoursesFragment]
    to [org.ionproject.android.course_details.CourseDetailsFragment] which is required to obtain the CourseDetails.
     */
    lateinit var courseDetailsUri: URI

    /**
     * [classSectionUri] is used to pass the uri from [org.ionproject.android.course_details.CourseDetailsFragment]
    and [org.ionproject.android.favorites.FavoritesFragment] to [org.ionproject.android.class_section.ClassSectionFragment]
    which is required to obtain the ClassSection.
     */
    lateinit var classSectionUri: URI

    /**
     * [programmeDetailsUri] is used to pass the uri from [org.ionproject.android.programmes.ProgrammesFragment]
    to [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment] which is required to obtain the ProgrammeDetails.
     */
    lateinit var programmeDetailsUri: URI

    /**
     * programme is used to pass data from [org.ionproject.android.programmeDetails.ProgrammeDetailsFragment]
    to [org.ionproject.android.courses.CoursesFragment]
     */
    lateinit var programmeOfferSummaries: List<ProgrammeOfferSummary>
    var curricularTerm: Int = 0


}
