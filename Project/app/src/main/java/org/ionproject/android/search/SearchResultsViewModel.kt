package org.ionproject.android.search

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

class SearchResultsViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val searchResultsLiveData = MutableLiveData<List<SearchResult>>()

    val searchResults: List<SearchResult>
        get() = searchResultsLiveData.value ?: emptyList()

    fun observeSearchResults(lifecycleOwner: LifecycleOwner, onUpdate: () -> Unit) {
        searchResultsLiveData.observe(lifecycleOwner, Observer {
            onUpdate()
        })
    }

    fun search(searchURI: URI, query: String) {
        viewModelScope.launch {
            val searchResult = searchRepository.search(searchURI, query)
            searchResultsLiveData.postValue(searchResult)
        }
    }
}