package org.ionproject.android.programmes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_programmes.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.ionwebapi.WEB_API_HOST
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import java.net.URI

class ProgrammesFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programmes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtaining view model
        val viewModel =
            ViewModelProvider(this, ProgrammesViewModelProvider())[ProgrammesViewModel::class.java]

        // Hide view, show progress bar
        val viewGroup = recyclerview_programmes_list.parent as ViewGroup
        viewGroup.startLoading()

        viewModel.getAllProgrammes(URI("$WEB_API_HOST${sharedViewModel.root!!.programmesUri.path}"))

        //Programmes list setup
        val adapter = ProgrammesListAdapter(viewModel, sharedViewModel)

        recyclerview_programmes_list.adapter = adapter
        recyclerview_programmes_list.layoutManager = LinearLayoutManager(context)

        viewModel.observeProgrammesLiveData(viewLifecycleOwner) {
            viewGroup.stopLoading() // Hide progress bar, show views
            adapter.notifyDataSetChanged()
        }

        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }

        // Adding divider between items in the list
        recyclerview_programmes_list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

}
