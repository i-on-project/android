package org.ionproject.android.offline.catalogTimetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_exam_schedule.*
import kotlinx.android.synthetic.main.fragment_catalog_timetable.*
import kotlinx.android.synthetic.main.fragment_catalog_timetable.view.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import java.util.*

class CatalogTimetableFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    private val timetableViewModel: CatalogTimetableViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatalogTimetableViewModelProvider()
        )[CatalogTimetableViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_timetable, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide view, show progress bar
        val viewGroup = catalog_timetable_recyclerView.parent as ViewGroup
        viewGroup.startLoading()

        val programme = sharedViewModel.selectedCatalogProgramme?.programmeName

        val term = sharedViewModel.selectedCatalogProgrammeTerm

        view.textview_catalog_timetable_programme.text = programme?.toUpperCase(Locale.ROOT)

        view.textview_catalog_timetable_term.text = term

        if (programme != null) {

            timetableViewModel.getCatalogTimetable(programme, term){

                catalog_timetable_recyclerView.adapter = CatalogLecturesListAdapter(it.classes, requireContext())
                catalog_timetable_recyclerView.layoutManager = LinearLayoutManager(context)

                viewGroup.stopLoading()
            }
        }

    }

}