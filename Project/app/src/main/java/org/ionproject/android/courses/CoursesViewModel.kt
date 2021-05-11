package org.ionproject.android.courses

import androidx.lifecycle.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ProgrammeOffer
import org.ionproject.android.common.model.ProgrammeOfferSummary
import org.ionproject.android.common.repositories.ProgrammesRepository
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogProgramme
import org.ionproject.android.offline.models.CatalogProgrammeTermInfo
import org.ionproject.android.offline.models.CatalogProgrammeTermInfoFile
import org.ionproject.android.offline.models.CatalogTerm
import java.net.URI

class CoursesViewModel(private val programmesRepository: ProgrammesRepository, private val catalogRepository: CatalogRepository) : ViewModel() {

    private val programmeOffersLiveData = MutableLiveData<List<ProgrammeOffer>>()

    fun observeCoursesLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: () -> Unit
    ) {
        programmeOffersLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }

    private var mandatoryCourses = emptyList<ProgrammeOffer>()
    private var optionalCourses = emptyList<ProgrammeOffer>()

    //Indicates if the current list in liveData is the mandatoryCourses
    private var areMandatory = true

    val programmeOffers: List<ProgrammeOffer>
        get() = programmeOffersLiveData.value ?: emptyList()


    /**
     * Launches multiple coroutines which will be obtaining programmeOffers and updating the live data.
     * The coroutines are launched with [kotlinx.coroutines.MainCoroutineDispatcher.immediate].
     */
    fun getAllCoursesFromCurricularTerm(
        programmeOfferSummaries: List<ProgrammeOfferSummary>,
        curricularTerm: Int
    ) {
        viewModelScope.launch {
            val deferredOffers = mutableListOf<Deferred<ProgrammeOffer?>>()
            //Launching parallel coroutines to increase the speed of the method execution
            for (programmeOfferSummary in programmeOfferSummaries) {
                if (programmeOfferSummary.termNumbers.contains(curricularTerm))
                    deferredOffers.add(async {
                        programmesRepository.getProgrammeOfferDetails(
                            programmeOfferSummary
                        )
                    })
            }

            val newMandatoryCourses = mutableListOf<ProgrammeOffer>()
            val newOptionalCourses = mutableListOf<ProgrammeOffer>()

            //Await for results and then separate courses in mandatory and optional
            deferredOffers.awaitAll().forEach {
                it?.let {
                    if (it.optional)
                        newOptionalCourses.add(it)
                    else
                        newMandatoryCourses.add(it)
                }
            }

            mandatoryCourses = newMandatoryCourses
            optionalCourses = newOptionalCourses

            programmeOffersLiveData.postValue(mandatoryCourses)
        }
    }

    /**
     * Update [programmeOffers] content from mandatory courses to optional courses
    or vice-versa.
     * @returns true if the new list type is mandatory else false
     */
    fun changeListType(): Boolean {
        areMandatory = !areMandatory
        programmeOffersLiveData.postValue(
            if (areMandatory)
                mandatoryCourses
            else
                optionalCourses
        )
        return areMandatory
    }
}
