package org.ionproject.android.offline.catalogTermFiles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_catalog_term_files.view.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import java.util.*

class CatalogTermFilesFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_term_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        val programme = sharedViewModel.selectedCatalogProgramme?.programmeName ?: ""

        val term = sharedViewModel.selectedCatalogProgrammeTerm

        view.textview_catalog_term_files_programme.text = programme.toUpperCase(Locale.ROOT)

        view.textview_catalog_term_files_term.text = term

        view.button_catalog_term_files_exam.setOnClickListener {
            navController.navigate(R.id.action_catalog_term_files_to_catalogExamScheduleFragment)
        }

        view.button_catalog_term_files_timetable.setOnClickListener {
            navController.navigate(R.id.action_catalog_term_files_to_catalog_timetable)
        }

        view.button_catalog_term_files_calendar.setOnClickListener {
            navController.navigate(R.id.action_catalog_term_files_to_catalog_calendar)
        }
    }
}