package org.ionproject.android.programmeDetails

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ProgrammeWithOffers
import org.ionproject.android.common.repositories.ProgrammesRepository
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.*
import java.net.URI

class ProgrammeDetailsViewModel(
    private val programmesRepository: ProgrammesRepository,
    private val catalogRepository: CatalogRepository
) :
    ViewModel() {

    val catalogProgrammeTerms: List<CatalogTerm>
        get() = catalogProgrammeTermsLiveData.value?.terms
            ?: emptyList()

    /**
     * Live data for the terms of the specified programme
     */
    private val catalogProgrammeTermsLiveData = MutableLiveData<CatalogProgrammeTerms>()

    /**
     *  Requests the details of a programme from the API
     *  and calls onResult once the result is available
     *
     *  @param programmeSummary summary representation of a programme
     *  @param callback to be executed once the programme details are available
     */
    fun getProgrammeDetails(
        programmeDetailsUri: URI,
        onResult: (ProgrammeWithOffers) -> Unit
    ) {
        viewModelScope.launch {
            programmesRepository.getProgrammeDetails(programmeDetailsUri)?.let(onResult)
        }
    }

    //-------Catalog functions
    /**
     * Requests the details of a programme (it's terms)
     * from the catalog
     */
    fun getCatalogProgramDetails(
        programmeDetailsLink: URI
    ) {
        viewModelScope.launch {

            val terms = catalogRepository.getCatalogProgrammeTerms(programmeDetailsLink)

            catalogProgrammeTermsLiveData.postValue(terms)
        }
    }

    fun observeCatalogTermsLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        catalogProgrammeTermsLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }
}
