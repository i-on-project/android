package org.ionproject.android.offline.catalogTimetable

import android.os.Bundle
import android.view.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_timetable.*
import kotlinx.android.synthetic.main.fragment_catalog_timetable.view.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import org.ionproject.android.offline.models.ClassesDetails
import java.util.*

class CatalogTimetableFragment : ExceptionHandlingFragment() {

    private val lecturesList = mutableListOf<ClassesDetails>()

    var adapter = context?.let { CatalogLecturesListAdapter(lecturesList, it) }

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
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_timetable, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        /**
         * Only have the search bar visible if the exam schedule fragment is visible
         */
        menu.findItem(R.id.catalog_action_search).isVisible = true

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Observes the live data in the shared view model;
         *
         * When the live data has a value, it means a query was input in the
         * search action and we have to filter the exams
         */
        sharedViewModel.observeSearchText(viewLifecycleOwner) {
            adapter?.filter?.filter(it)
        }

        // Hide view, show progress bar
        val viewGroup = view as ViewGroup
        viewGroup.startLoading()

        val programme = sharedViewModel.selectedCatalogProgramme?.programmeName

        val term = sharedViewModel.selectedCatalogProgrammeTerm

        view.textview_catalog_timetable_programme.text = programme?.toUpperCase(Locale.ROOT)

        view.textview_catalog_timetable_term.text = term

        if (programme != null) {

            timetableViewModel.getCatalogTimetable(programme, term) {

                /**
                Sort the classes alphabetically
                 */
                lecturesList.addAll(it.classes.sortedBy { classesDetails -> classesDetails.acr })

                adapter = CatalogLecturesListAdapter(lecturesList, requireContext())

                catalog_timetable_recyclerView.adapter = adapter

                catalog_timetable_recyclerView.layoutManager = LinearLayoutManager(context)

                viewGroup.stopLoading()
            }
        }

    }

}