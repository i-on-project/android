package org.ionproject.android.search

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider


class SearchResultsFragment : Fragment() {

    // This view model is shared between fragments and the MainActivity
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    /**
     * Get the [SearchResultsViewModel] which should contain the search query results
     */
    private val searchResultsViewModel: SearchResultsViewModel by activityViewModels {
        SearchResultsViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // We just want to make a Toast of the search query text, for now...
        sharedViewModel.observeQueryText(this) { query ->
            hideKeyboard(view)
            searchResultsViewModel.search(sharedViewModel.root.searchUri, query)
        }
    }

    // Hide keyboard when user finished typing his query text
    fun hideKeyboard(view: View) {
        // Get the input Manager
        val imm: InputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

}
