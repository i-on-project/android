package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_courses.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel

class CatalogCoursesListAdapter(
    private val courses: List<String>,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<CatalogCoursesListAdapter.CatalogCourseViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogCourseViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_courses, parent, false)
        return CatalogCourseViewHolder(view, sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CatalogCourseViewHolder, position: Int) {
        holder.bindTo(courses[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = courses.size

    // Provides a reference to the views for each data item
    class CatalogCourseViewHolder(
        private val view: View,
        private val sharedViewModel: SharedViewModel
    ) : RecyclerView.ViewHolder(view) {

        private val courseButton = view.button_courses_list_item_course

        fun bindTo(course: String) {
            courseButton.text = course
            courseButton.setOnClickListener {
                sharedViewModel.selectedCourse = course
                view.findNavController().navigate(R.id.action_courses_to_course_details)
            }
        }
    }
}