package edu.isel.ion.android.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R
import edu.isel.ion.android.SharedViewModel
import edu.isel.ion.android.common.model.CourseSummary

class CoursesListAdapter(private val model : CoursesViewModel, private val sharedViewModel: SharedViewModel) :
    RecyclerView.Adapter<CoursesListAdapter.CourseViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_courses, parent, false)
        return CourseViewHolder(view,sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bindTo(model.courses[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = model.courses.size

    // Provides a reference to the views for each data item
    class CourseViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) : RecyclerView.ViewHolder(view) {

        private val courseName = view.findViewById<TextView>(R.id.textview_courses_list_item_course)

        fun bindTo(courseSummary: CourseSummary) {
            courseName.text = courseSummary.acronym
            view.setOnClickListener {
                sharedViewModel.courseSummaryLiveData.postValue(courseSummary)
                view.findNavController().navigate(R.id.action_courses_to_course_details)
            }
        }
    }
}