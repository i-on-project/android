package edu.isel.ion.android.courses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R
import edu.isel.ion.android.SharedViewModel
import edu.isel.ion.android.SharedViewModelProvider

class CoursesFragment : Fragment() {

    /*
        This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel : SharedViewModel by activityViewModels {
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
            .of(this,CoursesViewModelProvider())[CoursesViewModel :: class.java]

        //Courses List Setup
        val coursesList = view.findViewById<RecyclerView>(R.id.recyclerview_courses_list)
        val coursesListAdapter = CoursesListAdapter(viewModel,sharedViewModel)
        coursesList.layoutManager = LinearLayoutManager(context)
        coursesList.adapter = coursesListAdapter

        //Request all courses from the WebAPI
        viewModel.getAllCourses()

        viewModel.observeCoursesLiveData(this) {
            coursesListAdapter.notifyDataSetChanged()
        }

    }

}
