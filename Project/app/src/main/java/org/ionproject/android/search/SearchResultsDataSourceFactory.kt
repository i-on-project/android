package org.ionproject.android.search

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository

class SearchResultsDataSourceFactory(
    private val searchRepository: SearchRepository
) : DataSource.Factory<Int, SearchResult>() {

    private val sourceLiveData = MutableLiveData<SearchResultsDataSource>()

    override fun create(): DataSource<Int, SearchResult> {
        val latestSource = SearchResultsDataSource(searchRepository)
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}