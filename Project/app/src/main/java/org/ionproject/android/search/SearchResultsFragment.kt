package org.ionproject.android.search

import android.os.Bundle
import android.provider.SearchRecentSuggestions
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

/**
 * A simple [Fragment] subclass.
 */
class SearchResultsFragment : Fragment() {

    // This view model is shared between fragments and the MainActivity
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
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
            Toast.makeText(context, "Query = $query", Toast.LENGTH_SHORT).show()
        }
    }

}
