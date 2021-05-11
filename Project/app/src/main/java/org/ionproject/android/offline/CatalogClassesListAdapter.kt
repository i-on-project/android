package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_classes.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.common.model.ClassSummary
import org.ionproject.android.offline.models.Section

class CatalogClassesListAdapter(
   private val sections:List<Section>,
   private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<CatalogClassesListAdapter.CatalogClassViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogClassViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return CatalogClassViewHolder(view, sharedViewModel)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CatalogClassViewHolder, position: Int) {
        holder.bindTo(sections[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = sections.size

    // Provides a reference to the views for each data item
    class CatalogClassViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) : RecyclerView.ViewHolder(view) {

        private val classItem = view.button_classes_list_item_class

        fun bindTo(section: Section) {
            classItem.text = section.section
            classItem.setOnClickListener {
                sharedViewModel.selectedClass = section
                //view.findNavController().navigate(R.id.action_course_details_to_class_section)
            }
        }
    }
}