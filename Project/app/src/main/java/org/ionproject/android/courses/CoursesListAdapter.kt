package org.ionproject.android.courses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_courses.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.common.model.ProgrammeOffer

class CoursesListAdapter(
    private val model: CoursesViewModel,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<CoursesListAdapter.CourseViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_courses, parent, false)
        return CourseViewHolder(view, sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        holder.bindTo(model.programmeOffers[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = model.programmeOffers.size

    // Provides a reference to the views for each data item
    class CourseViewHolder(
        private val view: View,
        private val sharedViewModel: SharedViewModel
    ) : RecyclerView.ViewHolder(view) {

        private val courseButton = view.button_courses_list_item_course

        /**
         * Binds the properties of a [CourseSummary] to a view [CourseViewHolder],
         * and sets a click listener to navigate to the course details
         */
        fun bindTo(programmeOffer: ProgrammeOffer) {
            courseButton.text = programmeOffer.acronym
            courseButton.setOnClickListener {
                sharedViewModel.courseDetailsUri = programmeOffer.detailsUri
                view.findNavController().navigate(R.id.action_courses_to_course_details)
            }
        }
    }
}
