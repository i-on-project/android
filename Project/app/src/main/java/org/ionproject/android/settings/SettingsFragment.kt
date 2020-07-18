package org.ionproject.android.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_settings.*
import org.ionproject.android.ExceptionHandlingFragment
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.model.CalendarTerm

class SettingsFragment : ExceptionHandlingFragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            SettingsViewModelProvider()
        )[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCalendarTermSpinner(spinner_settings_calendar_terms)
    }

    private fun setupCalendarTermSpinner(spinner: Spinner) {
        viewModel.getAllCalendarTermsFromFavorites()

        val spinnerAdapter = ArrayAdapter<CalendarTerm>(
            requireContext(), R.layout.support_simple_spinner_dropdown_item
        )

        spinner.adapter = spinnerAdapter

        viewModel.observeCalendarTerms(viewLifecycleOwner) { calendarTerms ->
            spinnerAdapter.clear() // Making sure that spinner has no information before adding new information
            spinnerAdapter.addAll(calendarTerms)
            val selectedCalendarTerm = viewModel.getSelectedCalendarTerm()

            if (selectedCalendarTerm != null) {
                /**
                 *  If a schedule calendar term has been saved on shared preferences file
                 *  then show it as the default value for spinner
                 */
                val idx = calendarTerms.indexOfFirst { it.name == selectedCalendarTerm }
                spinner.setSelection(if (idx > -1) idx else 0)
            }
        }

        /**
         * Ensures that when a calendar term is selected in the spinner
         * it will be saved to a shared preferences file, in order for
         * schedule fragment show information about this calendar term
         */
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
                viewModel.setSelectedCalendarTerm(selectedItem.name)
            }
        }
    }
}