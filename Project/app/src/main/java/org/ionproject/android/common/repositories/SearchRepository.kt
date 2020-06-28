package org.ionproject.android.common.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ionproject.android.common.dto.SirenEntity
import org.ionproject.android.common.ionwebapi.IIonWebAPI
import org.ionproject.android.common.model.SearchResultType
import org.ionproject.android.search.toSearchResults
import java.net.URI

class SearchRepository(private val ionWebAPI: IIonWebAPI) {

    /**
     * Requests all resources which match the [query] from the uri
     * [Uri]
     */
    suspend fun search(searchUri: URI, query: String, page: Int, limit: Int) =
        withContext(Dispatchers.IO) {
            ionWebAPI.getFromURI(
                searchUri.addQueryStrings(
                    "query" to listOf(query),
                    "types" to listOf(
                        SearchResultType.CLASS_SECTION.toString(),
                        SearchResultType.PROGRAMME.toString(),
                        SearchResultType.COURSE.toString()
                    ),
                    "page" to listOf(page.toString()),
                    "limit" to listOf(limit.toString())
                ), SirenEntity::class.java
            ).toSearchResults()
        }
}

private fun URI.addQueryStrings(vararg queryStrings: Pair<String, List<String>>): URI {
    return this
}
