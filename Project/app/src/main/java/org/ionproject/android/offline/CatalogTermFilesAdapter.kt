package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_courses.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.courses.CoursesViewModel
import org.ionproject.android.offline.models.CatalogProgrammeTermInfoFile

class CatalogTermFilesAdapter(
    private val model: CoursesViewModel
) : RecyclerView.Adapter<FilesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FilesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_courses, parent, false)
        return FilesViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        holder.bindTo(model.catalogTermfiles[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = model.catalogTermfiles.size

}

class FilesViewHolder(
    view: View
) : RecyclerView.ViewHolder(view) {

    private val courseButton = view.button_courses_list_item_course

    fun bindTo(catalogProgrammeTermInfoFile: CatalogProgrammeTermInfoFile) {
        courseButton.text =
            catalogProgrammeTermInfoFile.fileName.split(".")[0] //remove .json from file name
        courseButton.setOnClickListener {
            //add new fragment to display file content
        }
    }
}