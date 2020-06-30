package org.ionproject.android.search

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

private const val FIRST_PAGE = 0

/**
 * The searchResultsDataSource which should fetch information from the web api.
 * This needs to extends from [PageKeyedDataSource] because our key is the number of the page
 * and the we want to obtain [SearchResult]s.
 */
class SearchResultsDataSource(
    private val searchRepository: SearchRepository,
    private val scope: CoroutineScope,
    private val searchUri: URI,
    private val query: String
) : PageKeyedDataSource<Int, SearchResult>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchResult>
    ) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(searchUri, query, FIRST_PAGE, params.requestedLoadSize)
            callback.onResult(result, null, FIRST_PAGE + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(searchUri, query, params.key, params.requestedLoadSize)
            val nextPage = if (result.isNotEmpty()) params.key + 1 else null
            callback.onResult(result, nextPage)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(searchUri, query, params.key, params.requestedLoadSize)
            val prevPage = if (params.key > 1) params.key - 1 else null
            callback.onResult(result, prevPage)
        }

    }
}