package org.ionproject.android.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_courses.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

class CoursesFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Obtaining view model
        val viewModel = ViewModelProviders
            .of(this, CoursesViewModelProvider())[CoursesViewModel::class.java]

        //Courses List Setup
        val coursesList = recyclerview_courses_list
        val coursesListAdapter = CoursesListAdapter(viewModel, sharedViewModel)
        coursesList.layoutManager = LinearLayoutManager(context)
        coursesList.adapter = coursesListAdapter

        //Request courses from a specific term from the WebAPI
        viewModel.getAllCoursesFromCurricularTerm(
            sharedViewModel.programme,
            sharedViewModel.curricularTerm
        )

        viewModel.observeCoursesLiveData(this) {
            coursesListAdapter.notifyDataSetChanged()
        }

        button_courses_optional_courses.setOnClickListener {
            if (viewModel.changeListType())
                (it as Button).text = "Optional courses"
            else
                (it as Button).text = "Mandatory courses"
        }

    }

}
