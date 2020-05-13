package org.ionproject.android.programmes

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.ProgrammeSummary
import org.ionproject.android.common.repositories.ProgrammesRepository

class ProgrammesViewModel(private val programmesRepository: ProgrammesRepository) : ViewModel() {

    val programmeSummaries: List<ProgrammeSummary> get() = programmesLiveData.value ?: emptyList()

    private val programmesLiveData = MutableLiveData<List<ProgrammeSummary>>()

    init {
        viewModelScope.launch {
            val programmes = programmesRepository.getAllProgrammes()
            programmesLiveData.postValue(programmes)
        }
    }

    fun observeProgrammesLiveData(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        programmesLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }
}