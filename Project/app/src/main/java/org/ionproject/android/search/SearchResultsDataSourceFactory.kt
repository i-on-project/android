package org.ionproject.android.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

class SearchResultsDataSourceFactory(
    private val searchRepository: SearchRepository,
    private val scope: CoroutineScope,
    private val searchURI: URI,
    private val query: String
) : DataSource.Factory<Int, SearchResult>() {

    // Creating mutable live data for our SearchResultsDataSource
    private val sourceLiveData = MutableLiveData<SearchResultsDataSource>()

    override fun create(): DataSource<Int, SearchResult> {
        val dataSource =
            SearchResultsDataSource(searchRepository, scope, searchURI, query) // Get our SearchResultsDataSource
        sourceLiveData.postValue(dataSource) // Post our data source to get the values
        return dataSource
    }

    // Getter for the data source
    fun getSearchResultsDataSource() = sourceLiveData.value
}