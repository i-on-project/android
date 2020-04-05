package edu.isel.ion.android.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import edu.isel.ion.android.R
import edu.isel.ion.android.SEARCH_KEY
import edu.isel.ion.android.SharedViewModel
import edu.isel.ion.android.SharedViewModelProvider

/**
 * A simple [Fragment] subclass.
 */
class SearchResultsFragment : Fragment() {

    /*
        This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel : SharedViewModel by activityViewModels {
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

        /*
        Obtaining search text from shared view model
         */
        sharedViewModel.searchText.observe(viewLifecycleOwner,Observer<String>{
            Toast.makeText(context,it,Toast.LENGTH_LONG).show()
        })
    }

}
