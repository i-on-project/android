package org.ionproject.android.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_courses.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.addSwipeRightGesture

class CoursesFragment : ExceptionHandlingFragment() {

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
        val viewModel =
            ViewModelProvider(this, CoursesViewModelProvider())[CoursesViewModel::class.java]

        //Courses List Setup
        val coursesListAdapter = CoursesListAdapter(viewModel, sharedViewModel)
        recyclerview_courses_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = coursesListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        //Request courses from a specific term from the WebAPI
        viewModel.getAllCoursesFromCurricularTerm(
            sharedViewModel.programmeOfferSummaries,
            sharedViewModel.curricularTerm
        )

        viewModel.observeCoursesLiveData(this) {
            coursesListAdapter.notifyDataSetChanged()
        }

        view.addSwipeRightGesture {
            findNavController().navigateUp()
        }

        button_courses_optional_courses.setOnClickListener {
            if (viewModel.changeListType())
                (it as Button).text = it.resources.getString(R.string.optional_courses)
            else
                (it as Button).text = it.resources.getString(R.string.mandatory_courses)
        }
    }
}
