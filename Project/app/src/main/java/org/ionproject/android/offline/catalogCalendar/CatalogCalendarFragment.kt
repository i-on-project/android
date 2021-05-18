package org.ionproject.android.offline.catalogCalendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_calendar.*
import kotlinx.android.synthetic.main.fragment_catalog_calendar.view.*
import kotlinx.android.synthetic.main.fragment_catalog_programmes.*
import kotlinx.android.synthetic.main.fragment_catalog_programmes.recyclerview_catalog_programmes_list
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import java.util.*

class CatalogCalendarFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: CatalogSharedViewModel by activityViewModels {
        CatalogSharedViewModelProvider()
    }

    private val calendarViewModel: CatalogCalendarViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CatalogCalendarViewModelProvider()
        )[CatalogCalendarViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_catalog_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide view, show progress bar
        val viewGroup = view as ViewGroup
        viewGroup.startLoading()

        val programme = sharedViewModel.selectedCatalogProgramme?.programmeName

        val term = sharedViewModel.selectedCatalogProgrammeTerm

        val year = sharedViewModel.selectedYear

        view.textview_catalog_calendar_program.text = programme?.toUpperCase(Locale.ROOT)

        if (programme != null) {
            calendarViewModel.getCatalogCalendar(year){

                val semesterNr = Integer.parseInt(term.split("-")[2]) //can be 1 or 2

                Log.d("Catalog", "semester: $semesterNr term: $term")

                val semesterContent = it.terms[semesterNr - 1] //terms has only two items in the list (0 and 1)

                view.textview_catalog_calendar_term.text = semesterContent.calendarTerm

                val interruptionsAdapter = CatalogCalendarInterruptionsAdapter(semesterContent.interruptions)
                view.recyclerview_catalog_calendar_interruptions.adapter = interruptionsAdapter
                view.recyclerview_catalog_calendar_interruptions.layoutManager = LinearLayoutManager(context)

                // Adding divider between items in the list
                recyclerview_catalog_calendar_interruptions.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                val evaluationsAdapter = CatalogCalendarEvaluationsAdapter(semesterContent.evaluations)
                view.recyclerview_catalog_calendar_evaluations.adapter = evaluationsAdapter
                view.recyclerview_catalog_calendar_evaluations.layoutManager = LinearLayoutManager(context)

                // Adding divider between items in the list
                recyclerview_catalog_calendar_evaluations.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                val detailsAdapter = CatalogCalendarDetailsAdapter(semesterContent.details)
                view.recyclerview_catalog_calendar_details.adapter = detailsAdapter
                view.recyclerview_catalog_calendar_details.layoutManager = LinearLayoutManager(context)

                // Adding divider between items in the list
                recyclerview_catalog_calendar_details.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                val otherEventsAdapter = CatalogCalendarInterruptionsAdapter(semesterContent.otherEvents)
                view.recyclerview_catalog_calendar_otherEvents.adapter = otherEventsAdapter
                view.recyclerview_catalog_calendar_otherEvents.layoutManager = LinearLayoutManager(context)

                // Adding divider between items in the list
                recyclerview_catalog_calendar_otherEvents.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                viewGroup.stopLoading()
            }
        }

    }
}