package org.ionproject.android.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.model.CalendarTerm

class FavoritesFragment : ExceptionHandlingFragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            FavoritesViewModelProvider()
        )[FavoritesViewModel::class.java]
    }

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFavoritesListBehaviour(recyclerview_favorites_classes_list)
        setupCalendarTermSpinner(spinner_favorites_calendar_terms)

        view.efab_favorites_add_favorite.setOnClickListener {
            findNavController().navigate(R.id.action_favorites_to_programmes)
        }
    }

    /**
     * Ensures the favorites list is updated with the favorites
     * inside the database.
     * Adds the swipe action to delete a favorite.
     */
    private fun setupFavoritesListBehaviour(favoritesList: RecyclerView) {
        favoritesList.layoutManager = LinearLayoutManager(context)

        val favoritesListAdapter = FavoritesListAdapter(viewModel, sharedViewModel)
        favoritesList.adapter = favoritesListAdapter
        favoritesList.addItemDecoration(
            DividerItemDecoration(
                this.context,
                DividerItemDecoration.VERTICAL
            )
        )

        context?.apply {
            val itemTouchHelper = ItemTouchHelper(
                FavoritesListAdapter.SwipeToDelete(viewModel, this)
            )
            itemTouchHelper.attachToRecyclerView(favoritesList)
        }

        viewModel.observeFavorites(viewLifecycleOwner) {
            favoritesListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Updates the spinner with the calendar terms from the local db
     * Ensures that when an item is selected, the favorites are updated
     * according to that calendar term
     */
    private fun setupCalendarTermSpinner(spinner: Spinner) {
        viewModel.getAllCalendarTermsFromFavorites()

        val spinnerAdapter = ArrayAdapter<CalendarTerm>(
            requireContext(), R.layout.support_simple_spinner_dropdown_item
        )
        spinner.adapter = spinnerAdapter
        viewModel.observeCalendarTerms(viewLifecycleOwner) {
            spinnerAdapter.clear() // Making sure that spinner has no information before adding new information
            spinnerAdapter.addAll(it)
        }

        //Ensures that when an item is selected in the spinner the favorites list is updated
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = spinner.getItemAtPosition(position) as CalendarTerm
                //Searching for the favorites of the selected calendar term
                viewModel.getFavoritesFromCalendarTerm(selectedItem)
            }

        }
    }

}
