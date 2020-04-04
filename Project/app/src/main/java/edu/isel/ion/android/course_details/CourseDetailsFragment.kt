package edu.isel.ion.android.course_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R

/**
 * A simple [Fragment] subclass.
 */
class CourseDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Obtaining View Model
        val viewModel = ViewModelProviders
            .of(this,CoursesDetailsViewModelProvider())[CourseDetailsViewModel::class.java]

        //Setting up all course details
        val courseFullName = view.findViewById<TextView>(R.id.textview_course_details_full_name)
        val courseYear = view.findViewById<TextView>(R.id.textview_course_details_year)
        val courseSemester = view.findViewById<TextView>(R.id.textview_course_details_semester)
        
        //Course classes list recycler view
        val classesList = view.findViewById<RecyclerView>(R.id.recyclerview_course_details_classes_list)
        classesList.layoutManager = LinearLayoutManager(context) //TODO Confirm if this is the right context
        classesList.adapter = ClassesListAdapter(viewModel)
    }

}
