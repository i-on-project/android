package org.ionproject.android.search

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

class SearchResultsViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    companion object {
        // Number of search results to be displayed per page request
        private const val pageSize = 10

        // Creating PagedList config
        private val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()
    }


    // Creating live data for PagedList
    private var searchResultsLiveData: LiveData<PagedList<SearchResult>>? = null

    val searchResults: List<SearchResult>
        get() = searchResultsLiveData?.value ?: emptyList()

    fun observeSearchResults(
        lifecycleOwner: LifecycleOwner,
        onUpdate: (PagedList<SearchResult>) -> Unit
    ) {
        searchResultsLiveData?.observe(lifecycleOwner, Observer {
            onUpdate(it)
        })
    }

    /**
     * Search for information about a query.
     * @param searchURI the uri to make the request
     * @param query query string to be used for searchURI
     */
    fun search(searchURI: URI, query: String) {
        // Creating a Data Source Factory
        val dataSourceFactory = object : DataSource.Factory<Int, SearchResult>() {
            override fun create(): DataSource<Int, SearchResult> =
                SearchResultsDataSource(searchRepository, viewModelScope, searchURI, query)
        }

        searchResultsLiveData = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
    }
}