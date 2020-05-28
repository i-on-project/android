package org.ionproject.android

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.dto.JsonHome
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.repositories.RootRepository

/**
 * This view model is used to shared information between fragments and the main activity,
 * the other approach is to use arguments, but that approach is not recommended by the android
 * docs if you are sharing objects.
 */
class SharedViewModel(rootRepository: RootRepository) : ViewModel() {

    // Search text used to pass data from search bar to searchResultFragment
    private val searchText = MutableLiveData<String>()

    fun observeQueryText(lifecycleOwner: LifecycleOwner, onUpdate: (String) -> Unit) =
        searchText.observe(lifecycleOwner, Observer { onUpdate(it) })

    fun setQueryText(searchQuery: String) {
        searchText.postValue(searchQuery)
    }


    // --------------- JSON HOME -----------------------
    private val jsonHomeLiveData = MutableLiveData<JsonHome>()

    init {
        viewModelScope.launch {
            val jsonHome = rootRepository.getJsonHome()
            jsonHomeLiveData.postValue(jsonHome)
        }
    }

    fun observeJsonHomeLiveData(lifecycleOwner: LifecycleOwner, onUpdate: (JsonHome) -> Unit) {
        jsonHomeLiveData.observe(lifecycleOwner, Observer { onUpdate(it) })
    }

    /**
     * [JsonHome] used check for the existence of resources
     */
    val jsonHome: JsonHome? get() = jsonHomeLiveData.value


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
