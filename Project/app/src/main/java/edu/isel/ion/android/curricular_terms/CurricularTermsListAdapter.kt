package edu.isel.ion.android.curricular_terms

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R

class CurricularTermsListAdapter(private val model : CurricularTermsViewModel) :
    RecyclerView.Adapter<CurricularTermsListAdapter.CurricularTermViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): CurricularTermViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.curricular_terms_list_item, parent, false)
        return CurricularTermViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: CurricularTermViewHolder, position: Int) {
        TODO()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = TODO()

    // Provides a reference to the views for each data item
    class CurricularTermViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        fun bindTo() {
            TODO()
        }
    }
}