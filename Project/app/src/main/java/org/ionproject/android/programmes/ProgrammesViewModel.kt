package org.ionproject.android.programmes

import androidx.lifecycle.*
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.repositories.ProgrammesRepository

class ProgrammesViewModel(private val programmesRepository: ProgrammesRepository) : ViewModel() {

    val programmeSummaries: List<ProgrammeSummary> get() = programmesLiveData.value ?: emptyList()

    private val programmesLiveData: LiveData<List<ProgrammeSummary>> = liveData {
        emit(programmesRepository.getAllProgrammes())
    }

    fun observeProgrammesLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        programmesLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }
}