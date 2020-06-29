package org.ionproject.android.search

import androidx.paging.PageKeyedDataSource
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository

private const val FIRST_PAGE = 0
private const val PAGE_SIZE = 10

/**
 * The searchResultsDataSource which should fetch information from the web api.
 * This needs to extends from [PageKeyedDataSource] because our key is the number of the page
 * and the we want to obtain [SearchResult]s.
 */
class SearchResultsDataSource(
    private val searchRepository: SearchRepository
) : PageKeyedDataSource<Int, SearchResult>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchResult>
    ) {
        TODO("Not yet implemented")
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        TODO("Not yet implemented")
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        TODO("Not yet implemented")
    }
}