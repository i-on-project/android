package org.ionproject.android.offline.catalogProgrammes

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogProgramme
import org.ionproject.android.offline.models.CatalogProgrammes

class CatalogProgrammesViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    val catalogProgrammes: List<CatalogProgramme> get() = catalogProgrammesLiveData.value?.programmes
        ?: emptyList()

    private val catalogProgrammesLiveData = MutableLiveData<CatalogProgrammes>()

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