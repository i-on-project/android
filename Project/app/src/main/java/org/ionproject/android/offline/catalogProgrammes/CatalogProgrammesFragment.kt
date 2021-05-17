package org.ionproject.android.offline.catalogProgrammes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_programmes.*
import kotlinx.android.synthetic.main.fragment_programmes.recyclerview_programmes_list
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider

class CatalogProgrammesFragment : ExceptionHandlingFragment() {

    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_programmes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtaining view model
        val viewModel = ViewModelProvider(this, CatalogProgrammesViewModelProvider())[CatalogProgrammesViewModel::class.java]

        // Hide view, show progress bar
        val viewGroup = recyclerview_catalog_programmes_list.parent as ViewGroup
        viewGroup.startLoading()

        viewModel.getCatalogProgrammes()

        val adapter = CatalogProgrammesListAdapter(viewModel,sharedViewModel)

        recyclerview_catalog_programmes_list.adapter = adapter
        recyclerview_catalog_programmes_list.layoutManager = LinearLayoutManager(context)

        viewModel.observeCatalogProgrammesLiveData(viewLifecycleOwner) {
            viewGroup.stopLoading() // Hide progress bar, show views
            adapter.notifyDataSetChanged()
        }

        // Adding divider between items in the list
        recyclerview_catalog_programmes_list.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}