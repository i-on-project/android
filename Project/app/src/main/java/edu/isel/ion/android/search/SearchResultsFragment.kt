package edu.isel.ion.android.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.isel.ion.android.R

/**
 * A simple [Fragment] subclass.
 */
class SearchResultsFragment : Fragment() {

    /*
    Search arguments
     */
    companion object args {
        val SEARCH_KEY = "1xn1nr010"
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
        val searchQuery = arguments?.getString(SEARCH_KEY)
    }

}
