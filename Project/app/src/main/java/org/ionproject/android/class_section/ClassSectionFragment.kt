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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_class_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            classIDTextView.text = it.name

            //Setup checkbox behaviour only after the details of the class are obtained
            setupCheckboxBehaviour(checkbox_class_section_favorite)

            // Get all exams for this course
            requestExamEvents()
        }
    }

    private fun requestExamEvents() {
        val examsList = recyclerview_class_section_exams
        val examsListAdapter = ExamsListAdapter(viewModel)

        examsList.layoutManager = LinearLayoutManager(context)
        examsList.adapter = examsListAdapter

        // TODO: Don't make hardcoded uris to get exams
        val uris = listOf(
            URI("/v0/courses/1/classes/1920v/calendar/1234"),
            URI("/v0/courses/1/classes/1920v/calendar/1235")
        )

        viewModel.getExams(uris) {
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
