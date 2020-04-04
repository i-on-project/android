package edu.isel.ion.android.course_details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R
import org.w3c.dom.Text

class ClassesListAdapter(model : CourseDetailsViewModel) :
    RecyclerView.Adapter<ClassesListAdapter.ClassViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClassViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return ClassViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        TODO()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = TODO()

    // Provides a reference to the views for each data item
    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val className = view.findViewById<TextView>(R.id.textview_classes_list_item_name)
        private val teacherName = view.findViewById<TextView>(R.id.textview_classes_list_item_teacher)

        fun bindTo() {
            TODO("Binding class summary")
        }
    }
}