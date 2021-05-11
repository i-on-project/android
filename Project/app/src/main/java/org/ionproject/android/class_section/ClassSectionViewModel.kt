package org.ionproject.android.class_section

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.Events
import org.ionproject.android.common.repositories.ClassesRepository
import org.ionproject.android.common.repositories.EventsRepository
import org.ionproject.android.common.repositories.FavoriteRepository
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogProgrammeTermInfoFile
import org.ionproject.android.offline.models.CatalogTerm
import org.ionproject.android.offline.models.ExamSchedule
import org.ionproject.android.offline.models.Timetable
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
        viewModelScope.launch {
            val events = eventsRepository.getAllEventsFromClassSection(
                classSection = classSection,
                getEvents = { eventsRepository.forceGetEvents(it) }
            ) {
                classesRepository.getClassCollectionByUri(it)
            }
            eventsLiveData.postValue(events)
        }
    }

    fun getEventsFromClassSection(classSection: ClassSection) {
        viewModelScope.launch {
            val events = eventsRepository.getAllEventsFromClassSection(
                classSection = classSection,
                getEvents = { eventsRepository.getEvents(it) }
            ) {
                classesRepository.getClassCollectionByUri(it)
            }
            eventsLiveData.postValue(events)
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

    //---------Catalog Methods---------
}
