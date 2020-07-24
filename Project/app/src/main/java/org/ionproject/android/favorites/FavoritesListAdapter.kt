package org.ionproject.android.favorites

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.list_item_classes.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.common.model.Favorite

class FavoritesListAdapter(
    private val model: FavoritesViewModel,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<FavoritesListAdapter.FavoriteViewHolder>() {

    /** Create new views (invoked by the layout manager) */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        /** create a new view */
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_classes, parent, false)
        return FavoriteViewHolder(view, sharedViewModel)
    }

    /** Replace the contents of a view (invoked by the layout manager) */
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bindTo(model.favorites[position])
    }

    /** Return the size of your dataset (invoked by the layout manager) */
    override fun getItemCount() = model.favorites.count()


    /** Provides a reference to the views for each item in the list*/
    class FavoriteViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) :
        RecyclerView.ViewHolder(view) {

        private val classItem = view.button_classes_list_item_class

        fun bindTo(favorite: Favorite) {
            classItem.text = view.resources.getString(
                R.string.label_favorites_placeholder_favorites,
                favorite.courseAcronym,
                favorite.id
            )
            classItem.setOnClickListener {
                sharedViewModel.classSectionUri = favorite.selfURI
                view.findNavController().navigate(R.id.action_favorites_to_class_section)
            }
        }
    }

    /** Helper class to add swipe action for each item in the recycler view */
    class SwipeToDelete(private val model: FavoritesViewModel, context: Context) :
        ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        private val colorDrawableBackground =
            ContextCompat.getDrawable(context, R.drawable.shape_square_left_round_corners)?.apply {
                setTint(ResourcesCompat.getColor(context.resources, R.color.errorColor, null))
            }
        private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24dp)

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val itemView = viewHolder.itemView
            val position = viewHolder.adapterPosition
            val favorite = model.favorites[position]
            model.deleteFavorite(favorite)
            Snackbar.make(
                itemView,
                itemView.resources.getText(R.string.label_favorites_delete_message_favorites),
                Snackbar.LENGTH_SHORT
            ).setAction(
                itemView.resources.getText(R.string.label_favorite_undo_favorites)
            ) {
                model.addFavorite(favorite)
            }.show()
        }

        /**
         * Overriding this method to add background and icon, while sliding
         * the view to the side as recommended here:
         * https://material.io/design/interaction/gestures.html#types-of-gestures
         */
        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            val itemView = viewHolder.itemView

            if (deleteIcon != null && colorDrawableBackground != null) {

                val iconMarginVertical =
                    (viewHolder.itemView.height - deleteIcon.intrinsicHeight) / 2

                colorDrawableBackground.setBounds(
                    itemView.left,
                    itemView.top,
                    dX.toInt(),
                    itemView.bottom
                )
                deleteIcon.setBounds(
                    itemView.left + iconMarginVertical,
                    itemView.top + iconMarginVertical,
                    itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMarginVertical
                )

                colorDrawableBackground.draw(c)
                deleteIcon.draw(c)
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}
