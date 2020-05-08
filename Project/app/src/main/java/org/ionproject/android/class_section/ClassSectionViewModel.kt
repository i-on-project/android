package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ExamSummary
import org.ionproject.android.common.model.JournalSummary
import org.ionproject.android.common.model.Lecture
import org.ionproject.android.common.model.TodoSummary
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
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
    private val examsLiveData = MutableLiveData<List<ExamSummary>>()
    private val workAssignmentsLiveData = MutableLiveData<List<TodoSummary>>()
    private val journalsLiveData = MutableLiveData<List<JournalSummary>>()

    // Public getter to return lecturesLiveData information
    val lectures: List<Lecture>
        get() = lecturesLiveData.value ?: emptyList()

    // Public getter to return examsLiveData information
    val exams: List<ExamSummary>
        get() = examsLiveData.value ?: emptyList()

    // Public getter to return workAssignments information
    val workAssignments: List<TodoSummary>
        get() = workAssignmentsLiveData.value ?: emptyList()

    // Public getter to return journals information
    val journals: List<JournalSummary>
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
            currClassSection = classSectionRepository.getClassSection(classSummary)
            onResult(currClassSection)
        }
    }

    /**
     * Gets all exams available for the [currClassSection] class section
     *
     * @param uris The uris to get information about the exams
     */
    fun getExams(uris: List<URI>) {
        viewModelScope.launch {
            val exams: List<ExamSummary> = uris.map { eventsRepository.getExamFromCourse(it) }
            examsLiveData.postValue(exams)
        }
    }

    /**
     * Gets all lectures available for the [currClassSection] class section
     *
     * @param uri The uri to get information about the lectures
     */
    fun getLectures(uri: URI?) {
        viewModelScope.launch {
            val lectures: List<Lecture> = eventsRepository.getLectures(uri)
            lecturesLiveData.postValue(lectures)
        }
    }

    /**
     * Gets all work assignments available for the [currClassSection] class section to be done
     *
     * @param uris The uri to get information about all work assignments to be done
     */
    fun getWorkAssignments(uris: List<URI>) {
        viewModelScope.launch {
            // TODO: workAssignments requests can be parallel
            val workAssignments = uris.map {
                eventsRepository.getWorkAssignment(it)
            }
            workAssignmentsLiveData.postValue(workAssignments)
        }
    }

    /**
     * Request all journals associated to a class section
     *
     * @param uris The uris to get information about the journals
     */
    fun getJournals(uris: List<URI>) {
        viewModelScope.launch {
            // TODO: journals request can be parallel
            val journals = uris.map {
                eventsRepository.getJournals(it)
            }
            journalsLiveData.postValue(journals)
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
            onResult
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
            classSectionRepository.addClassSectionToDb(currClassSection)
            favoriteRepository.addFavorite(classSummary)
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
            favoriteRepository.removeFavorite(classSummary)
            classSectionRepository.removeClassSectionFromDb(currClassSection)
        }
    }

    /**
     * Checks if this class section is a favorite
     */
    fun isThisClassFavorite(classSummary: ClassSummary, onUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            onUpdate(
                favoriteRepository.favoriteExists(classSummary)
            )
        }
    }

}
