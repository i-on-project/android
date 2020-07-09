package org.ionproject.android.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ionproject.android.common.IonApplication

class SearchResultsViewModelProvider : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            SearchResultsViewModel::class.java -> SearchResultsViewModel(IonApplication.searchRepository)
            else -> throw IllegalArgumentException("Class $modelClass is not valid for this provider")
        } as T
    }
}