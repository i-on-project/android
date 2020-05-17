package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.*
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository
import java.net.URI

class ClassSectionViewModel(
    private val eventsRepository: EventsRepository,
    private val classSectionRepository: ClassesRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    /**
     * Current class section obtained from Web API
     */
    private lateinit var currClassSection: ClassSection

    /**
     * All Livedatas used to store information returned by sending
     * a request to the Repositories.
     * These livedatas should be private and we must have public getters in order
     * to return information holden by these livedatas.
     */
    private val lecturesLiveData = MutableLiveData<List<Lecture>>()
    private val examsLiveData = MutableLiveData<List<Exam>>()
    private val workAssignmentsLiveData = MutableLiveData<List<Todo>>()
    private val journalsLiveData = MutableLiveData<List<Journal>>()

    // Public getter to return lecturesLiveData information
    val lectures: List<Lecture>
        get() = lecturesLiveData.value ?: emptyList()

    // Public getter to return examsLiveData information
    val exams: List<Exam>
        get() = examsLiveData.value ?: emptyList()

    // Public getter to return workAssignments information
    val workAssignments: List<Todo>
        get() = workAssignmentsLiveData.value ?: emptyList()

    // Public getter to return journals information
    val journals: List<Journal>
        get() = journalsLiveData.value ?: emptyList()

    /**
     * Obtains a classSection details information from the [classSectionRepository]
     * and update UI with the result
     *
     * @param classSummary The class summary's details to be collected
     * @param onResult callback to be called when the classSection details has been collected
     */
    fun getClassSectionDetails(classSummary: ClassSummary, onResult: (ClassSection) -> Unit) {
        viewModelScope.launch {
            classSectionRepository.getClassSection(classSummary)?.let {
                currClassSection = it
                onResult(it)
            }
        }
    }

    fun getEvents(uri: URI) {
        viewModelScope.launch {
            val events = eventsRepository.getEvents(uri)
            examsLiveData.postValue(events.exams)
            lecturesLiveData.postValue(events.lectures)
            workAssignmentsLiveData.postValue(events.todos)
            journalsLiveData.postValue(events.journals)
        }
    }

    /**
     * Observes if [examsLiveData]'s information has changed.
     *
     * @param lifeCycle The activity's lifecycle
     * @param onResult callback to be called when [examsLiveData]'s information has changed
     */
    fun observeExamsList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        examsLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    /**
     * Observes if [lecturesLiveData]'s information has changed.
     *
     * @param lifeCycle The activity's lifecycle
     * @param onResult callback to be called when [lecturesLiveData]'s information has changed
     */
    fun observeLecturesList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        lecturesLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    /**
     * Observes if [workAssignmentsLiveData]'s information has changed.
     *
     * @param lifeCycle The activity's lifecycle
     * @param onResult callback to be called when [workAssignmentsLiveData]'s information has changed
     */
    fun observeWorkAssignmentsList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        workAssignmentsLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    /**
     * Observes if [journalsLiveData]'s information has changed.
     *
     * @param lifeCycle The activity's lifecycle
     * @param onResult callback to be called when [journalsLiveData]'s information has changed
     */
    fun observerJournalsList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        journalsLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    /**
     * Adds the current classSection to the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun addClassToFavorites(classSummary: ClassSummary) {
        viewModelScope.launch {
            favoriteRepository.addClassToFavorites(classSummary)
        }
    }

    /**
     * Removes the current classSection from the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun removeClassFromFavorites(classSummary: ClassSummary) {
        viewModelScope.launch {
            favoriteRepository.removeClassFromFavorites(classSummary)
        }
    }

    /**
     * Checks if this class section is a favorite
     */
    fun isThisClassFavorite(classSummary: ClassSummary, onUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            onUpdate(
                favoriteRepository.isClassFavorite(classSummary)
            )
        }
    }

}
