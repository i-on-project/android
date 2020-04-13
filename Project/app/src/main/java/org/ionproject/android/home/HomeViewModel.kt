package org.ionproject.android.home

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.db.Suggestion
import org.ionproject.android.common.repositories.SuggestionsMockRepository

class HomeViewModel(private val repository: SuggestionsMockRepository) : ViewModel() {

    private val suggestionsLiveData = MutableLiveData<List<Suggestion>>()

    val suggestions : List<Suggestion>
        get() = suggestionsLiveData.value ?: emptyList()

    fun observeSuggestionsLiveData(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (List<Suggestion>) -> Unit
    ) {
        suggestionsLiveData.observe(lifecycleOwner, Observer {
            if(it != null)
                onUpdate(it)
        })
    }

    fun getAllSuggestions() = repository.getSuggestions()

    fun insertMocks() {
        viewModelScope.launch {
            repository.insertSuggestionMocks()
        }

    }
}