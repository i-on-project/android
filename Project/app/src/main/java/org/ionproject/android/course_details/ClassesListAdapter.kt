package org.ionproject.android.course_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_classes.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.common.model.ClassSummary

class ClassesListAdapter(
    private val model: CourseDetailsViewModel,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<ClassesListAdapter.ClassViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return ClassViewHolder(view, sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bindTo(model.classesList[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = model.classesList.size

    // Provides a reference to the views for each data item
    class ClassViewHolder(
        private val view: View,
        private val sharedViewModel: SharedViewModel
    ) : RecyclerView.ViewHolder(view) {

        private val classItem = view.button_classes_list_item_class

        fun bindTo(classSummary: ClassSummary) {
            classItem.text = classSummary.id
            classItem.setOnClickListener {
                sharedViewModel.classSectionUri = classSummary.detailsUri
                view.findNavController().navigate(R.id.action_course_details_to_class_section)
            }
        }
    }
}
