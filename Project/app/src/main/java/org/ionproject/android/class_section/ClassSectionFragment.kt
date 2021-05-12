package org.ionproject.android.class_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_class_section.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading
import org.ionproject.android.offline.CatalogLecturesListAdapter
import java.net.URI
import java.util.*

class ClassSectionFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    /**
     * Obtaining Class Section's View Model
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ClassSectionViewModelProvider()
        )[ClassSectionViewModel::class.java]
    }

    /**
     * Uri used to obtain the class section
     */
    private val classSectionUri: URI by lazy(LazyThreadSafetyMode.NONE) {
        val classSectionUri = sharedViewModel.classSectionUri
        classSectionUri
            ?: throw IllegalArgumentException("ClassSectionUri is missing! Cannot load ClassSectionFragment without it.")
    }

    /**
     * Lectures List's adapter
     */
    private val lecturesListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        LecturesListAdapter(viewModel)
    }

    /**
     * Exams List's adapter
     */
    private val examsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ExamsListAdapter(viewModel)
    }

    /**
     * Work assignments's adapter
     */
    private val workAssignmentsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TodoAdapter(viewModel)
    }

    /**
     * Journals's adapter
     */
    private val journalsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        JournalsAdapter(viewModel)
    }

    /**
     * ViewGroup which contains all this fragments views
     */
    lateinit var viewGroup: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adds loading
        viewGroup = view as ViewGroup
        viewGroup.startLoading()

        if (sharedViewModel.selectedCatalogProgramme == null) { //regular API info, the catalog vars are null
            setupLecturesList()
            setupExamsList()
            setupWorkAssignmentsList()
            setupJournalsList()
            setupClassSectionDetails()
            view.addSwipeRightGesture {
                findNavController().navigateUp()
            }
            setupSectionsBehaviour()
            setupRefreshButtonBehaviour()

        } else {

            val lectures =
                viewModel.getCatalogLecturesForSelectedClass(
                    sharedViewModel.selectedCourse!!,
                    sharedViewModel.selectedClass!!,
                    sharedViewModel
                )

            //setup the top elements
            textView_class_section_calendar_term.text = sharedViewModel.selectedCatalogProgrammeTerm?.term
            textView_class_section_class.text = sharedViewModel.selectedClass
            textView_class_section_course.text = sharedViewModel.selectedCatalogProgramme!!.programmeName.toUpperCase(Locale.ROOT)

            //setup lectures list for specified class
            val lecturesList = recyclerview_class_section_lectures
            lecturesList.layoutManager = LinearLayoutManager(context)
            lecturesList.adapter = CatalogLecturesListAdapter(lectures)

            viewGroup.stopLoading()
        }
    }

    /**
     * Requests the details of the current class and updates
     * the respective UI elements with them.
     */
    private fun setupClassSectionDetails() {

        // Class Section View Holder Setup
        val courseTextView = textView_class_section_course
        val classTermTextView = textView_class_section_class
        val calendarTermTextView = textView_class_section_calendar_term
        val checkBox = checkbox_class_section_favorite

        // Search for Class Section Details
        viewModel.getClassSectionDetails(classSectionUri) {
            courseTextView.text = it.courseAcronym
            classTermTextView.text = it.id
            calendarTermTextView.text = it.calendarTerm

            //Setup checkbox behaviour only after the details of the class are obtained
            setupCheckboxBehaviour(checkBox, it)

            requestEvents(it)
        }
    }

    /**
     * Setup the class section's lectures list with [lecturesListAdapter] as his adapter.
     */
    private fun setupLecturesList() {
        val lecturesList = recyclerview_class_section_lectures
        lecturesList.layoutManager = LinearLayoutManager(context)
        lecturesList.adapter = lecturesListAdapter
    }

    /**
     * Setup the class section's exams list with [examsListAdapter] as his adapter.
     */
    private fun setupExamsList() {
        val examsList = recyclerview_class_section_exams
        examsList.layoutManager = LinearLayoutManager(context)
        examsList.adapter = examsListAdapter
    }

    /**
     * Setup the class section's work assignments list with [workAssignmentsAdapter] as his adapter.
     */
    private fun setupWorkAssignmentsList() {
        val assignmentsList = recyclerview_class_section_todos
        assignmentsList.layoutManager = LinearLayoutManager(context)
        assignmentsList.adapter = workAssignmentsAdapter
    }

    /**
     * Setup the class section's journal list with [journalsAdapter] as his adapter.
     */
    private fun setupJournalsList() {
        val journalsList = recyclerview_class_section_journals
        journalsList.layoutManager = LinearLayoutManager(context)
        journalsList.adapter = journalsAdapter
    }

    /**
     * Request all events available for the class section [currClassSummary]
     */
    private fun requestEvents(
        classSection: ClassSection
    ) {
        viewModel.getEventsFromClassSection(classSection)
        viewModel.observeEvents(this) {
            examsListAdapter.notifyDataSetChanged()
            lecturesListAdapter.notifyDataSetChanged()
            workAssignmentsAdapter.notifyDataSetChanged()
            journalsAdapter.notifyDataSetChanged()
            viewGroup.stopLoading()
        }
    }

    /**
     * Defines what happens when the checkbox is clicked
     */
    private fun setupCheckboxBehaviour(
        checkBox: CheckBox,
        classSection: ClassSection
    ) {
        //If the checks box was checked before, then it must be updated
        viewModel.isThisClassFavorite(classSection) {
            checkBox.isChecked = it
        }
        checkBox.setOnClickListener {
            val isChecked = checkBox.isChecked
            if (isChecked) {
                viewModel.addClassToFavorites(classSection)
            } else {
                viewModel.removeClassFromFavorites(classSection)
            }
        }
    }

    private fun setupSectionsBehaviour() {
        textView_class_section_lectures.setOnClickListener {
            if (recyclerview_class_section_lectures.visibility == View.VISIBLE)
                recyclerview_class_section_lectures.visibility = View.GONE
            else
                recyclerview_class_section_lectures.visibility = View.VISIBLE
        }

        textView_class_section_exams.setOnClickListener {
            if (recyclerview_class_section_exams.visibility == View.VISIBLE)
                recyclerview_class_section_exams.visibility = View.GONE
            else
                recyclerview_class_section_exams.visibility = View.VISIBLE
        }

        textView_class_section_todos.setOnClickListener {
            if (recyclerview_class_section_todos.visibility == View.VISIBLE)
                recyclerview_class_section_todos.visibility = View.GONE
            else
                recyclerview_class_section_todos.visibility = View.VISIBLE
        }

        textView_class_section_journals.setOnClickListener {
            if (recyclerview_class_section_journals.visibility == View.VISIBLE)
                recyclerview_class_section_journals.visibility = View.GONE
            else
                recyclerview_class_section_journals.visibility = View.VISIBLE
        }
    }

    private fun setupRefreshButtonBehaviour() {
        button_class_section_refresh.setOnClickListener {
            viewGroup.startLoading()

            // Class Section View Holder Setup
            val courseTextView = textView_class_section_course
            val classTermTextView = textView_class_section_class
            val calendarTermTextView = textView_class_section_calendar_term

            // Search for Class Section Details
            viewModel.forceGetClassSectionDetails(classSectionUri) {
                courseTextView.text = it.courseAcronym
                classTermTextView.text = it.id
                calendarTermTextView.text = it.calendarTerm

                viewModel.forceGetEventsFromClassSection(it)
            }
        }
    }
}
