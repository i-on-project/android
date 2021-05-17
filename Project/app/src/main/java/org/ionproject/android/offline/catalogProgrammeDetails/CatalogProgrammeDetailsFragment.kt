package org.ionproject.android.offline.catalogProgrammeDetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_programme_details.*
import kotlinx.android.synthetic.main.fragment_programme_details.*
import kotlinx.android.synthetic.main.fragment_programme_details.textview_programme_details_name
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import org.ionproject.android.programmeDetails.ProgrammeDetailsViewModel
import org.ionproject.android.programmeDetails.ProgrammeDetailsViewModelProvider
import org.ionproject.android.programmeDetails.TermsListAdapter
import java.net.URI
import java.util.*

class CatalogProgrammeDetailsFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    private val programmeViewModel: CatalogProgrammeDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatalogProgrammeDetailsViewModelProvider()
        )[CatalogProgrammeDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_programme_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewGroup = catalog_programme_details_recyclerView.parent as ViewGroup
        viewGroup.startLoading()

        programmeViewModel.getCatalogAcademicYears()

        programmeViewModel.observeAcademicYearsLiveData(viewLifecycleOwner) {

            catalog_programme_details_recyclerView.adapter =
                CatalogYearsListAdapter(programmeViewModel, sharedViewModel)

            catalog_programme_details_recyclerView.layoutManager = LinearLayoutManager(context)

            // Adding dividers between items in the grid
            catalog_programme_details_recyclerView.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
            viewGroup.stopLoading()
        }

    }
}