package org.ionproject.android.search

import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.ionproject.android.common.model.SearchResult
import org.ionproject.android.common.repositories.SearchRepository
import java.net.URI

private const val FIRST_PAGE = 0

/**
 * The SearchResultsDataSource which should fetch information from the web api.
 * This needs to extend from [PageKeyedDataSource] because the number of the page is our key
 * in order to obtain [SearchResult]s.
 */
class SearchResultsDataSource(
    private val searchRepository: SearchRepository,
    private val scope: CoroutineScope,
    private val searchUri: URI,
    private val query: String
) : PageKeyedDataSource<Int, SearchResult>() {

    /**
     * Initial search request. This method should make a search request for the first page.
     * @param params contains data that can be used to make request
     * @param callback callback to pass the data received by the request and information about this page's position
     */
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, SearchResult>
    ) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(
                    searchUri,
                    query,
                    FIRST_PAGE,
                    SearchResultsViewModel.PAGE_SIZE
                )
            callback.onResult(result, null, FIRST_PAGE + 1)
        }
    }

    /**
     * Next page search request.
     * This method should make a search request for the next page.
     * @param params contains data that can be used to make request
     * @param callback callback to pass the data received and if exists next page information
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(
                    searchUri,
                    query,
                    params.key,
                    SearchResultsViewModel.PAGE_SIZE
                )
            val nextPage = if (result.isNotEmpty()) params.key + 1 else null
            callback.onResult(result, nextPage)
        }
    }

    /**
     * Previous page search request.
     * This method should make a search request for the previous page.
     * @param params contains data that can be used to make request
     * @param callback callback to pass the data received and if exists the previous page information.
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchResult>) {
        scope.launch {
            val result: List<SearchResult> =
                searchRepository.search(
                    searchUri,
                    query,
                    params.key,
                    SearchResultsViewModel.PAGE_SIZE
                )
            val prevPage = if (params.key > 1) params.key - 1 else null
            callback.onResult(result, prevPage)
        }
    }
}