package org.ionproject.android.course_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_course_details.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

class CourseDetailsFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
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

        // Obtaining View Model
        val viewModel = ViewModelProviders
            .of(this, CoursesDetailsViewModelProvider())[CourseDetailsViewModel::class.java]

        // Setting up all course details
        val courseFullName = textview_course_details_full_name
        val courseYear = textview_course_details_year
        val courseSemester = textview_course_details_semester

        viewModel.getCourseDetails(sharedViewModel.courseSummary) {
            courseFullName.text = it.name
            courseYear.text = "1ºAno" //TODO Get course year
            courseSemester.text = "1ºSemestre" //TODO Get course term
            viewModel.getCourseClasses(it)
        }

        //Course classes list recycler view
        val classesList = recyclerview_course_details_classes_list
        //TODO: Confirm if this is the right context
        classesList.layoutManager = LinearLayoutManager(context)

        val classesListAdapter = ClassesListAdapter(viewModel, sharedViewModel)
        classesList.adapter = classesListAdapter

        viewModel.observeClassesListLiveData(viewLifecycleOwner) {
            classesListAdapter.notifyDataSetChanged()
        }
    }

}
