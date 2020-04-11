package org.ionproject.android.search

import android.content.Context
import android.content.SearchRecentSuggestionsProvider
import android.provider.SearchRecentSuggestions

class RecentSuggestionsProvider : SearchRecentSuggestionsProvider() {
    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "org.ionproject.android.search.RecentSuggestionsProvider"
        const val MODE: Int = DATABASE_MODE_QUERIES

        fun clearHistory(context: Context?) {
            SearchRecentSuggestions(context, AUTHORITY, MODE).clearHistory()
        }
    }

}