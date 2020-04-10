package org.ionproject.android.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_classes.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.Favorite

class FavoritesListAdapter(private val model: FavoritesViewModel) :
    RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder>() {

    /** Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        /** create a new view */
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return FavoriteViewHolder(view)
    }

    /** Replace the contents of a view (invoked by the layout manager) */
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindTo(model.favorites[position])
    }

    //Removes and item from the list
    private fun deleteItem(position: Int) {
        model.deleteFavorite(model.favorites[position])
        notifyItemRemoved(position)
    }

    /** Return the size of your dataset (invoked by the layout manager) */
    override fun getItemCount() = model.favorites.count()


    /** Provides a reference to the views for each data item */
    class FavoriteViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val classItem = view.button_classes_list_item_class

        fun bindTo(favorite: Favorite) {
            classItem.text = view.resources.getString(
                R.string.label_favorites_placeholder,
                favorite.course,
                favorite.classSection
            )
        }
    }

    class SwipeToDelete(private val favoritesListAdapter: FavoritesListAdapter) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            favoritesListAdapter.deleteItem(position)
        }
    }
}
