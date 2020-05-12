package org.ionproject.android.course_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_course_details.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.model.CalendarTerm
import org.ionproject.android.common.model.Course

class CourseDetailsFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders
            .of(this, CoursesDetailsViewModelProvider())[CourseDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCourseDetails()
        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }
    }

    /**
     * Obtains the details of a course from the i-on web API and updates the
     * UI with the result. Once the details are obtained then it obtains its classes
     * according to the selected calendar term in the spinner.
     */
    private fun setupCourseDetails() {
        // Setting up all course details
        val courseFullName = textview_course_details_full_name
        val courseYear = textview_course_details_year
        val courseSemester = textview_course_details_semester

        viewModel.getCourseDetails(sharedViewModel.courseSummary) {
            courseFullName.text = it.name
            courseYear.text = "1ºAno" //TODO Get course year
            courseSemester.text = "1ºSemestre" //TODO Get course term
            setupCourseClassesList(recyclerview_course_details_classes_list)
            setupCalendarTermSpinner(spinner_course_details_calendar_terms, it)
        }
    }

    private fun setupCourseClassesList(classesList: RecyclerView) {
        //TODO: Confirm if this is the right context
        classesList.layoutManager = LinearLayoutManager(context)
        classesList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        val classesListAdapter = ClassesListAdapter(viewModel, sharedViewModel)
        classesList.adapter = classesListAdapter

        viewModel.observeClassesListLiveData(viewLifecycleOwner) {
            classesListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Updates the spinner with the calendar terms from the local db
     * Ensures that when an item is selected, the favorites are updated
     * according to that calendar term
     */
    private fun setupCalendarTermSpinner(spinner: Spinner, course: Course) {
        val spinnerAdapter = ArrayAdapter<CalendarTerm>(
            requireContext(), R.layout.support_simple_spinner_dropdown_item
        )
        spinner.adapter = spinnerAdapter
        viewModel.observeCalendarTerms(viewLifecycleOwner) {
            spinnerAdapter.addAll(it)
        }

        //Ensures that when an item is selected in the spinner the favorites list is updated
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = viewModel.calendarTerms[position]
                //Obtaining the classes from that course
                viewModel.getClassesFromCourse(course, selectedItem)
            }

        }
    }

}
