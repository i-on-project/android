package org.ionproject.android.search

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

class SearchResultsViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {
    private val pageSize = 10

    // Creating live data for PagedList
    private var searchResultsLiveData: LiveData<PagedList<SearchResult>>? = null

    // Creating live data for data source
    private var searchResultsDataSource: SearchResultsDataSource? = null

    private fun createDataSource(searchURI: URI, query: String) {
        // Get the dataSourceFactory
        val dataSourceFactory = SearchResultsDataSourceFactory(searchRepository, viewModelScope, searchURI, query)

        // Get the Live Data DataSource
        searchResultsDataSource = dataSourceFactory.getSearchResultsDataSource()

        // Getting PagedList config
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        searchResultsLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }


    val searchResults: List<SearchResult>
        get() = searchResultsLiveData?.value ?: emptyList()

    fun observeSearchResults(lifecycleOwner: LifecycleOwner, onUpdate: (PagedList<SearchResult>) -> Unit) {
        searchResultsLiveData?.observe(lifecycleOwner, Observer {
            onUpdate(it)
        })
    }

    fun search(searchURI: URI, query: String) {
        viewModelScope.launch {
            createDataSource(searchURI, query)
        }
    }
}