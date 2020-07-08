package org.ionproject.android.search

import android.content.SearchRecentSuggestionsProvider

/**
 * We need our [SearchSuggestionsProvider] to extend [SearchRecentSuggestionsProvider].
 * The [SearchRecentSuggestionsProvider] class automatically does the following steps:
 *  - The system takes the search query text and performs a query to our [SearchSuggestionsProvider]
 *  that contains the suggestions.
 *  - [SearchSuggestionsProvider] returns a Cursor that points to all suggestions that match the search query text.
 *  - The system displays the list of suggestions provided by the Cursor.
 */
class SearchSuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        // The content provider authority. In this case, is SearchSuggestionsProvider
        const val AUTHORITY = "org.ionproject.android.search.SearchSuggestionsProvider"

        // The database mode. This will present a one line suggestion query
        const val MODE: Int = DATABASE_MODE_QUERIES
    }

}