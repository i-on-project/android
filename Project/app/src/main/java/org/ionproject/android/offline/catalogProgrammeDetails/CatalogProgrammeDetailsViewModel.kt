package org.ionproject.android.offline.catalogProgrammeDetails

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.config.GservicesValue.value
import kotlinx.coroutines.launch
import org.ionproject.android.offline.CatalogRepository
import org.ionproject.android.offline.models.CatalogAcademicYear
import org.ionproject.android.offline.models.CatalogAcademicYears
import org.ionproject.android.offline.models.CatalogProgrammeTerms
import org.ionproject.android.offline.models.CatalogTerm
import java.net.URI

class CatalogProgrammeDetailsViewModel(private val catalogRepository: CatalogRepository): ViewModel() {

    val catalogAcademicYears: List<CatalogAcademicYear>
        get() = catalogAcademicYearsLiveData.value?.years
            ?: emptyList()

    /**
     * Live data for the terms of the specified programme
     */
    private val catalogAcademicYearsLiveData = MutableLiveData<CatalogAcademicYears>()

    /**
     * Requests the details of a programme (it's terms)
     * from the catalog
     */
    fun getCatalogAcademicYears(
    ) {
        viewModelScope.launch {

            val years = catalogRepository.getCatalogAcademicYears()

            catalogAcademicYearsLiveData.postValue(years)
        }
    }

    fun observeAcademicYearsLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        catalogAcademicYearsLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }
}