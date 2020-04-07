package edu.isel.ion.android.course_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R
import edu.isel.ion.android.SharedViewModel
import edu.isel.ion.android.common.model.ClassSummary

class ClassesListAdapter(
    private val model: CourseDetailsViewModel,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<ClassesListAdapter.ClassViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return ClassViewHolder(view,sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bindTo(model.classesList[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = model.classesList.size

    // Provides a reference to the views for each data item
    class ClassViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) : RecyclerView.ViewHolder(view) {

        private val className = view.findViewById<TextView>(R.id.textview_classes_list_item_name)
        private val teacherName = view.findViewById<TextView>(R.id.textview_classes_list_item_teacher)

        fun bindTo(classSummary: ClassSummary) {
            className.text = classSummary.id
            teacherName.text = "Paulo Pereira" //TODO Get teacher name
            view.setOnClickListener {
                sharedViewModel.classSummary = classSummary
                view.findNavController().navigate(R.id.action_course_details_to_class_section)
            }
        }
    }
}