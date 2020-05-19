package org.ionproject.android.programmeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.model.ProgrammeWithOffers
import org.ionproject.android.common.repositories.ProgrammesRepository

class ProgrammeDetailsViewModel(private val programmesRepository: ProgrammesRepository) :
    ViewModel() {

    /**
     *  Requests the details of a programme from the API
     *  and calls onResult once the result is available
     *
     *  @param programmeSummary summary representation of a programme
     *  @param callback to be executed once the programme details are available
     */
    fun getCourseDetails(
        programmeSummary: ProgrammeSummary?,
        onResult: (ProgrammeWithOffers) -> Unit
    ) {
        if (programmeSummary != null)
            viewModelScope.launch {
                programmesRepository.getProgrammeDetails(programmeSummary).let(onResult)
            }
    }


}