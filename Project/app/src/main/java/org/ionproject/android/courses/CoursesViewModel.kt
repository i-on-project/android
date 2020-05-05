package org.ionproject.android.courses

import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.Programme
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.repositories.ProgrammesRepository

class CoursesViewModel(private val programmesRepository: ProgrammesRepository) : ViewModel() {

    private val programmeOffersLiveData = MutableLiveData<List<ProgrammeOffer>>()

    fun observeCoursesLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: () -> Unit
    ) {
        programmeOffersLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }

    /**
     * This should be checked, might not work
     */
    private val mandatoryCourses = mutableListOf<ProgrammeOffer>()
    private val optionalCourses = mutableListOf<ProgrammeOffer>()

    private var areMandatory = true

    val programmeOffers: List<ProgrammeOffer>
        get() = programmeOffersLiveData.value ?: emptyList()

    /**
     * Launches multiplie coroutines which will be obtaining programmeOffers and updating the live data.
     * The coroutines are launched with [kotlinx.coroutines.MainCoroutineDispatcher.immediate].
     */
    fun getAllCoursesFromCurricularTerm(programme: Programme, curricularTerm: Int) {
        viewModelScope.launch {
            val deferredOffers = mutableListOf<Deferred<ProgrammeOffer>>()
            //Launching parallel coroutines to increase HTTP requests efficiency
            for (programmeOfferSummary in programme.programmeOffers) {
                if (programmeOfferSummary.termNumber == curricularTerm)
                    deferredOffers.add(async {
                        programmesRepository.getProgrammeOfferDetails(
                            programmeOfferSummary
                        )
                    })
            }
            //Await for results and then separate courses in mandatory and optional
            deferredOffers.awaitAll().forEach {
                if (it.optional)
                    optionalCourses.add(it)
                else
                    mandatoryCourses.add(it)
            }
            programmeOffersLiveData.postValue(mandatoryCourses)
        }
    }

    /**
     * Update [programmeOffers] content from mandatory courses to optional courses
    or vice-versa
     */
    fun changeListType(): Boolean {
        areMandatory = !areMandatory
        if (areMandatory)
            programmeOffersLiveData.postValue(mandatoryCourses)
        else
            programmeOffersLiveData.postValue(optionalCourses)
        return areMandatory
    }
}
