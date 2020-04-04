package edu.isel.ion.android.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.isel.ion.android.R

class FavoritesListAdapter(model : FavoritesViewModel) :
    RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        // create a new view
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.classes_list_item, parent, false)
        return FavoriteViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        TODO()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = TODO()

    // Provides a reference to the views for each data item
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindTo() {
            TODO()
        }
    }
}
