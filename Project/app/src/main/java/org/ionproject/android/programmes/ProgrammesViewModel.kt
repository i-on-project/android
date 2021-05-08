package org.ionproject.android.programmes

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.repositories.ProgrammesRepository
import org.ionproject.android.offline.models.CatalogProgrammes
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogProgramme
import java.net.URI

class ProgrammesViewModel(private val programmesRepository: ProgrammesRepository, private val catalogRepository: CatalogRepository) : ViewModel() {

    val programmeSummaries: List<ProgrammeSummary> get() = programmesLiveData.value ?: emptyList()

    val catalogProgrammes: List<CatalogProgramme> get() = catalogProgrammesLiveData.value?.programmes
        ?: emptyList()

    val programmesLiveData = MutableLiveData<List<ProgrammeSummary>>()

    val catalogProgrammesLiveData = MutableLiveData<CatalogProgrammes>()

    fun getAllProgrammes(programmesUri: URI) {
        viewModelScope.launch {
            val programmes = programmesRepository.getAllProgrammes(programmesUri)
            programmesLiveData.postValue(programmes)
        }
    }

    fun observeProgrammesLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        programmesLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }

    fun getCatalogProgrammes(){
        viewModelScope.launch {
            val catalogProgrammes = catalogRepository.getCatalogProgrammeList()
            catalogProgrammesLiveData.postValue(catalogProgrammes)
        }
    }

    fun observeCatalogProgrammesLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        catalogProgrammesLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }
}