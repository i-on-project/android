package org.ionproject.android.class_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_class_section.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.model.ClassSection
import org.ionproject.android.common.model.ClassSummary
import java.net.URI

class ClassSectionFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    /**
     * Class passed via SharedViewmodel
     */
    private val currClassSummary: ClassSummary by lazy {
        sharedViewModel.classSummary
    }

    /**
     * Obtaining Class Section's View Model
     */
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            ClassSectionViewModelProvider()
        )[ClassSectionViewModel::class.java]
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
        WorkAssignmentsAdapter(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLecturesList()
        setupExamsList()
        setupWorkAssignmentsList()
        setupClassSectionDetails()
    }

    /**
     * Requests the details of the current class and updates
     * the respective UI elements with them.
     */
    private fun setupClassSectionDetails() {
        // Class Section View Holder Setup
        val courseTextView = textView_class_section_course
        val classTermTextView = textView_class_section_class
        val classIDTextView = textView_class_section_id

        // Search for Class Section Details
        viewModel.getClassSectionDetails(currClassSummary) {
            courseTextView.text = it.course
            classTermTextView.text = it.calendarTerm
            classIDTextView.text = it.id

            //Setup checkbox behaviour only after the details of the class are obtained
            setupCheckboxBehaviour(checkbox_class_section_favorite)

            // Get all lectures for this class section
            requestLectureEvents(it)

            // Get all exams for this course
            requestExamEvents()

            // Get all work assignments for this class section
            requestWorkAssignments()
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
     * Setup the class section's work assignments with []
     */
    private fun requestWorkAssignments() {
        // TODO: Don't make hard coded uris, for now we only have 1 work assignment mock
        val uriMock = URI("/v0/courses/1/classes/1920v/calendar/123490")

        val uris = listOf<URI>(uriMock)
        viewModel.getWorkAssignments(uris)
        viewModel.observeWorkAssignmentsList(this) {
            workAssignmentsAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Gets all lectures for this class section
     */
    private fun requestLectureEvents(classSection: ClassSection) {
        viewModel.getLectures(classSection.calendarURI)
        viewModel.observeLecturesList(this) {
            lecturesListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Gets all exams for this class
     */
    private fun requestExamEvents() {
        // TODO: Don't make hardcoded uris to get exams
        val uris = listOf(
            URI("/v0/courses/1/classes/1920v/calendar/1234"),
            URI("/v0/courses/1/classes/1920v/calendar/1235")
        )

        viewModel.getExams(uris)
        viewModel.observeExamsList(this) {
            examsListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Defines what happens when the checkbox is clicked
     */
    private fun setupCheckboxBehaviour(checkBox: CheckBox) {
        //If the checks box was checked before, then it must be updated
        viewModel.isThisClassFavorite(currClassSummary) {
            checkBox.isChecked = it
        }
        checkBox.setOnClickListener {
            val isChecked = checkBox.isChecked
            if (isChecked) {
                viewModel.addClassToFavorites(currClassSummary)
            } else {
                viewModel.removeClassFromFavorites(currClassSummary)
            }
        }
    }
}
