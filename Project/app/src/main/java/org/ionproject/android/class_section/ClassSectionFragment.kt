package org.ionproject.android.class_section

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_class_section.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
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
        ViewModelProvider(
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
        TodoAdapter(viewModel)
    }

    /**
     * Journals's adapter
     */
    private val journalsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        JournalsAdapter(viewModel)
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
        setupJournalsList()
        setupClassSectionDetails()
        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }
        setupSectionsBehaviour()
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
        viewModel.getClassSectionDetails(currClassSummary) {
            courseTextView.text = it.courseAcronym
            classTermTextView.text = it.id.toString()
            calendarTermTextView.text = it.calendarTerm

            //Setup checkbox behaviour only after the details of the class are obtained
            setupCheckboxBehaviour(checkBox)

            requestEvents(it.calendarURI)
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
    private fun requestEvents(uri: URI?) {
        if (uri == null)
            return
        viewModel.getEvents(uri)

        viewModel.observeExamsList(this) { examsListAdapter.notifyDataSetChanged() }
        viewModel.observeLecturesList(this) { lecturesListAdapter.notifyDataSetChanged() }
        viewModel.observeWorkAssignmentsList(this) { workAssignmentsAdapter.notifyDataSetChanged() }
        viewModel.observerJournalsList(this) { journalsAdapter.notifyDataSetChanged() }
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


}
