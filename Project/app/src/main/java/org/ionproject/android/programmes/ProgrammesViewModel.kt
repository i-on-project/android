package org.ionproject.android.programmes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import org.ionproject.android.common.model.Programme
import org.ionproject.android.common.repositories.ProgrammesRepository

class ProgrammesViewModel(private val programmesRepository : ProgrammesRepository) : ViewModel() {

    val programmes: List<Programme>

    private val programmesLiveData : LiveData<List<Programme>> = liveData {
        emit(programmesRepository.getAllProgrammes())
    }
}