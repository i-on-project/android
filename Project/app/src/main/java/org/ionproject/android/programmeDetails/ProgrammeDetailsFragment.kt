package org.ionproject.android.programmeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_programme_details.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading

class ProgrammeDetailsFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    private val programmeViewModel: ProgrammeDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ProgrammeDetailsViewModelProvider()
        )[ProgrammeDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programme_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewGroup = view as ViewGroup
        viewGroup.startLoading()

        // Adding dividers between items in the grid
        recyclerview_programme_details_terms.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        programmeViewModel.getProgrammeDetails(sharedViewModel.programmeDetailsUri) {
            viewGroup.stopLoading()
            // Name is not mandatory (as mencioned in Core Docs https://github.com/i-on-project/core/blob/master/docs/api/read/courses.md)
            textview_programme_details_name.text =
                it.programme.name
                    ?: resources.getString(R.string.label_name_not_available_all)
            textview_programme_details_acronym.text = it.programme.acronym
            recyclerview_programme_details_terms.layoutManager = GridLayoutManager(context, 2)
            recyclerview_programme_details_terms.adapter =
                TermsListAdapter(it.programme.termSize, sharedViewModel)
            sharedViewModel.programmeOfferSummaries =
                it.programmeOffers //Sharing programme here otherwise it have to be passed to TermsListAdapter

        }

        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }
    }
}
