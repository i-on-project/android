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
    private val classesRepository: ClassesRepository,
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
    private val eventsLiveData = MutableLiveData<Events>()

    // Public property to return eventsLiveData information
    val events: Events
        get() = eventsLiveData.value ?: Events.NO_EVENTS

    /**
     * Obtains a classSection details information from the [classesRepository]
     * and update UI with the result
     *
     * @param classSummary The class summary's details to be collected
     * @param onResult callback to be called when the classSection details has been collected
     */
    fun getClassSectionDetails(classSectionUri: URI, onResult: (ClassSection) -> Unit) {
        viewModelScope.launch {
            classesRepository.getClassSection(classSectionUri)?.let {
                currClassSection = it
                onResult(it)
            }
        }
    }


    fun forceGetClassSectionDetails(classSectionUri: URI, onResult: (ClassSection) -> Unit) {
        viewModelScope.launch {
            classesRepository.forceGetClassSection(classSectionUri).let {
                currClassSection = it
                onResult(it)
            }
        }
    }


    fun forceGetEventsFromClassSection(classSection: ClassSection) {
        getEventsFromClassSection(classSection) {
            eventsRepository.forceGetEvents(it)
        }
    }


    fun getEventsFromClassSection(classSection: ClassSection) {
        getEventsFromClassSection(classSection) {
            eventsRepository.getEvents(it)
        }
    }

    private fun getEventsFromClassSection(
        classSection: ClassSection,
        getEvents: suspend (URI) -> Events?
    ) {
        viewModelScope.launch {
            val classCollection = classesRepository.getClassCollectionByUri(classSection.upURI)

            val exams = mutableListOf<Exam>()
            val lectures = mutableListOf<Lecture>()
            val todos = mutableListOf<Todo>()
            val journals = mutableListOf<Journal>()

            suspend fun getEventsAndAddToLists(uri: URI?) {
                if (uri != null && uri.path != "") {
                    val events = getEvents(uri)
                    events?.apply {
                        exams.addAll(events.exams)
                        lectures.addAll(events.lectures)
                        todos.addAll(events.todos)
                        journals.addAll(events.journals)
                    }
                }
            }

            if (classCollection != null)
                getEventsAndAddToLists(classCollection.fields.calendarURI)
            getEventsAndAddToLists(classSection.calendarURI)

            eventsLiveData.postValue(
                Events.create(
                    exams,
                    lectures,
                    todos,
                    journals

                )
            )
        }
    }


    /**
     * Observes if [eventsLiveData]'s information has changed.
     *
     * @param lifeCycle The activity's lifecycle
     * @param onResult callback to be called when [eventsLiveData]'s information has changed
     */
    fun observeEvents(lifeCycle: LifecycleOwner, onResult: () -> Unit) {
        eventsLiveData.observe(lifeCycle, Observer {
            onResult()
        })
    }

    /**
     * Adds the current classSection to the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun addClassToFavorites(classSection: ClassSection) {
        viewModelScope.launch {
            favoriteRepository.addClassToFavorites(classSection)
        }
    }

    /**
     * Removes the current classSection from the favorites list.
     * If the liveData hasn't been updated with the result from the API request, it returns false,
     * otherwise true. This is required because the user might click the checkbox before the API
     * returns the result.
     */
    fun removeClassFromFavorites(classSection: ClassSection) {
        viewModelScope.launch {
            favoriteRepository.removeClassFromFavorites(classSection)
        }
    }

    /**
     * Checks if this class section is a favorite
     */
    fun isThisClassFavorite(classSection: ClassSection, onUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            onUpdate(
                favoriteRepository.isClassFavorite(classSection)
            )
        }
    }

}
