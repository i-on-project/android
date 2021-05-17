package org.ionproject.android.offline.catalogExamSchedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_exam_schedule.*
import kotlinx.android.synthetic.main.fragment_catalog_exam_schedule.view.*
import kotlinx.android.synthetic.main.fragment_catalog_programmes.*
import kotlinx.android.synthetic.main.fragment_catalog_term_files.view.*
import kotlinx.android.synthetic.main.fragment_catalog_term_files.view.textview_catalog_term_files_programme
import kotlinx.android.synthetic.main.fragment_catalog_term_files.view.textview_catalog_term_files_term
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import org.ionproject.android.offline.catalogProgrammeDetails.CatalogProgrammeDetailsViewModel
import org.ionproject.android.offline.catalogProgrammeDetails.CatalogProgrammeDetailsViewModelProvider
import org.ionproject.android.offline.catalogProgrammes.CatalogProgrammesListAdapter
import org.ionproject.android.offline.catalogProgrammes.CatalogProgrammesViewModel
import org.ionproject.android.offline.catalogProgrammes.CatalogProgrammesViewModelProvider
import java.util.*

class CatalogExamScheduleFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    private val examViewModel: CatalogExamScheduleViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatalogExamScheduleViewModelProvider()
        )[CatalogExamScheduleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_exam_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide view, show progress bar
        val viewGroup = catalog_exam_schedule_recyclerView.parent as ViewGroup
        viewGroup.startLoading()

        val programme = sharedViewModel.selectedCatalogProgramme?.programmeName

        val term = sharedViewModel.selectedCatalogProgrammeTerm

        view.textview_catalog_exam_programme.text = programme?.toUpperCase(Locale.ROOT)

        view.textview_catalog_exam_term.text = term

        if (programme != null) {
            examViewModel.getCatalogExamSchedule(programme, term){

                val adapter = CatalogExamsListAdapter(it.exams)

                catalog_exam_schedule_recyclerView.adapter = adapter
                catalog_exam_schedule_recyclerView.layoutManager = LinearLayoutManager(context)

                viewGroup.stopLoading()
            }
        }

    }
}