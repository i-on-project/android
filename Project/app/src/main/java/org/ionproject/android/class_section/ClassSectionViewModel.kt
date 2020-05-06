package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.ExamSummary
import org.ionproject.android.common.Lecture
import org.ionproject.android.common.TodoSummary
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

    private val lecturesLiveData = MutableLiveData<List<Lecture>>()
    private val examsLiveData = MutableLiveData<List<ExamSummary>>()
    private val workAssignmentsLiveData = MutableLiveData<List<TodoSummary>>()

    val lectures: List<Lecture>
        get() = lecturesLiveData.value ?: emptyList()

    val exams: List<ExamSummary>
        get() = examsLiveData.value ?: emptyList()

    val workAssignments: List<TodoSummary>
        get() = workAssignmentsLiveData.value ?: emptyList()

    /**
     * Obtains a classSection from WebApi and updated the UI with the result
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
            // TODO: Don't make hard coded uris, for now we only have 1 lecture mock
            val uriMock = URI("/v0/courses/1/classes/1920v/11D/calendar")
            val lectures: List<Lecture> = eventsRepository.getLectures(uriMock)
            lecturesLiveData.postValue(lectures)
        }
    }

    /**
     * Gets all work assignments available for the [currClassSection] class section to be done
     *
     * @param uri The uri to get information about all work assignments to be done
     */
    fun getWorkAssignments(uri: List<URI>) {
        viewModelScope.launch {
            // TODO: workAssignments requests can be parallel
            val workAssignments = uri.map {
                eventsRepository.getWorkAssignment(it)
            }
            workAssignmentsLiveData.postValue(workAssignments)
        }
    }

    fun observeExamsList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        examsLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    fun observeLecturesList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        lecturesLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    fun observeWorkAssignmentsList(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        workAssignmentsLiveData.observe(lifeCycle, Observer {
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
