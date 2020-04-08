package org.ionproject.android.course_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.ionproject.android.R
import kotlin.coroutines.CoroutineContext

/**
 * A simple [Fragment] subclass.
 */
class CourseDetailsFragment : Fragment(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    /*
        This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: _root_ide_package_.org.ionproject.android.SharedViewModel by activityViewModels {
        _root_ide_package_.org.ionproject.android.SharedViewModelProvider()
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

        //Obtaining View Model
        val viewModel = ViewModelProviders
            .of(this, CoursesDetailsViewModelProvider())[CourseDetailsViewModel::class.java]

        //Setting up all course details
        val courseFullName = view.findViewById<TextView>(R.id.textview_course_details_full_name)
        val courseYear = view.findViewById<TextView>(R.id.textview_course_details_year)
        val courseSemester = view.findViewById<TextView>(R.id.textview_course_details_semester)

        viewModel.getCourseDetails(sharedViewModel.courseSummary) {
            courseFullName.text = it.name
            courseYear.text = "1ºAno" //TODO Get course year
            courseSemester.text = "1ºSemestre" //TODO Get course term
            viewModel.getCourseClasses(it)
        }

        //Course classes list recycler view
        val classesList =
            view.findViewById<RecyclerView>(R.id.recyclerview_course_details_classes_list)
        classesList.layoutManager =
            LinearLayoutManager(context) //TODO Confirm if this is the right context
        val classesListAdapter = ClassesListAdapter(viewModel, sharedViewModel)
        classesList.adapter = classesListAdapter

        viewModel.observeClassesListLiveData(viewLifecycleOwner) {
            classesListAdapter.notifyDataSetChanged()
        }
    }


}
