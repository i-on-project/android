package org.ionproject.android.offline.catalogCalendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_catalog_calendar.*
import kotlinx.android.synthetic.main.fragment_catalog_calendar.view.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.CatalogSharedViewModelProvider
import org.ionproject.android.offline.models.CalendarEvent
import org.ionproject.android.offline.models.Details
import org.ionproject.android.offline.models.Evaluation
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
            calendarViewModel.getCatalogCalendar(year) {

                val semesterNr = Integer.parseInt(term.split("-")[2]) //can be 1 or 2

                val semesterContent =
                    it.terms[semesterNr - 1] //terms has only two items in the list (0 and 1)

                view.textview_catalog_calendar_term.text = semesterContent.calendarTerm

                setupInterruptions(semesterContent.interruptions)

                setupEvaluations(semesterContent.evaluations)

                setupDetails(semesterContent.lectures)

                setupOtherEvents(semesterContent.otherEvents)

                viewGroup.stopLoading()
            }
        }

    }

    private fun setupInterruptions(interruptions: List<CalendarEvent>) {

        val interruptionsAdapter = CatalogCalendarInterruptionsAdapter(interruptions)
        view?.recyclerview_catalog_calendar_interruptions?.adapter = interruptionsAdapter
        view?.recyclerview_catalog_calendar_interruptions?.layoutManager =
            LinearLayoutManager(context)

        // Adding divider between items in the list
        recyclerview_catalog_calendar_interruptions.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

    }

    private fun setupEvaluations(evaluations: List<Evaluation>) {

        val evaluationsAdapter = CatalogCalendarEvaluationsAdapter(evaluations)
        view?.recyclerview_catalog_calendar_evaluations?.adapter = evaluationsAdapter
        view?.recyclerview_catalog_calendar_evaluations?.layoutManager =
            LinearLayoutManager(context)

        // Adding divider between items in the list
        recyclerview_catalog_calendar_evaluations.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupDetails(details: List<Details>) {

        val detailsAdapter = CatalogCalendarDetailsAdapter(details)
        view?.recyclerview_catalog_calendar_details?.adapter = detailsAdapter
        view?.recyclerview_catalog_calendar_details?.layoutManager = LinearLayoutManager(context)

        // Adding divider between items in the list
        recyclerview_catalog_calendar_details.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun setupOtherEvents(events: List<CalendarEvent>) {
        val otherEventsAdapter = CatalogCalendarInterruptionsAdapter(events)
        view?.recyclerview_catalog_calendar_otherEvents?.adapter = otherEventsAdapter
        view?.recyclerview_catalog_calendar_otherEvents?.layoutManager =
            LinearLayoutManager(context)

        // Adding divider between items in the list
        recyclerview_catalog_calendar_otherEvents.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }
}