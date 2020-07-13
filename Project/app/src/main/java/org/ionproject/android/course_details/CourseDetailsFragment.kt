package org.ionproject.android.course_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_course_details.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture
import org.ionproject.android.common.model.Classes
import org.ionproject.android.common.model.Course
import org.ionproject.android.common.startLoading
import org.ionproject.android.common.stopLoading

class CourseDetailsFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            CoursesDetailsViewModelProvider()
        )[CourseDetailsViewModel::class.java]
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

        // Adds loading
        val viewGroup = view as ViewGroup
        viewGroup.startLoading()

        setupCourseDetails(viewGroup)
        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }
    }

    /**
     * Obtains the details of a course from the i-on web API and updates the
     * UI with the result. Once the details are obtained then it obtains its classes
     * according to the selected calendar term in the spinner.
     */
    private fun setupCourseDetails(viewGroup: ViewGroup) {
        // Setting up all course details
        val courseFullName = textview_course_details_full_name
        val courseAcronym = textview_course_details_acronym

        viewModel.getCourseDetails(sharedViewModel.courseDetailsUri) {
            courseFullName.text = it.name
            courseAcronym.text = it.acronym
            setupCourseClassesList(recyclerview_course_details_classes_list)
            setupCalendarTermSpinner(spinner_course_details_calendar_terms, it)
            viewGroup.stopLoading()
        }
    }

    private fun setupCourseClassesList(classesList: RecyclerView) {
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
        // There is no point in obtaining the classes if there aren't any
        if (course.classesUri != null) {
            viewModel.getClasses(course.classesUri)

            val spinnerAdapter = ArrayAdapter<Classes>(
                requireContext(), R.layout.support_simple_spinner_dropdown_item
            )
            spinner.adapter = spinnerAdapter
            viewModel.observeClasses(viewLifecycleOwner) {
                spinnerAdapter.clear() // Making sure that spinner has no information before adding new information
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
                    val selectedItem = viewModel.classes[position]
                    //Obtaining the classes from that course
                    viewModel.getClassesFromCourse(selectedItem.selfUri)
                }
            }
        }
    }

}
