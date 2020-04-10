package org.ionproject.android.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.ionproject.android.R
import org.ionproject.android.common.model.CalendarTerm

/**
 * A simple [Fragment] subclass.
 */
class FavoritesFragment : Fragment() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            FavoritesViewModelProvider()
        )[FavoritesViewModel::class.java]
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
    }

    private fun setupFavoritesListBehaviour(favoritesList: RecyclerView) {
        //TODO Confirm if this is the right context
        favoritesList.layoutManager = LinearLayoutManager(context)

        val favoritesListAdapter = FavoritesListAdapter(viewModel)
        favoritesList.adapter = favoritesListAdapter

        val itemTouchHelper = ItemTouchHelper(
            FavoritesListAdapter.SwipeToDelete(favoritesListAdapter))
        itemTouchHelper.attachToRecyclerView(favoritesList)

        var count = 2

        viewModel.observeFavorites(viewLifecycleOwner) {
            if(count > 0) {
                favoritesListAdapter.notifyDataSetChanged()
                --count
            }
        }
    }

    private fun setupCalendarTermSpinner(spinner: Spinner) {
        val spinnerAdapter = ArrayAdapter<CalendarTerm>(
            requireContext(), R.layout.support_simple_spinner_dropdown_item
        )
        spinner.adapter = spinnerAdapter
        viewModel.observeCalendarTerms(viewLifecycleOwner) {
            spinnerAdapter.addAll(it)
            spinnerAdapter.notifyDataSetChanged()
            viewModel.getFavoritesFromCalendarTerm(it[0]) //Search for favorites
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
                val selectedItem = viewModel.calendarTerms[position]
                viewModel.getFavoritesFromCalendarTerm(selectedItem)
            }

        }
    }

}
