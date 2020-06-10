package org.ionproject.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

class HomeFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtaining view model
        val viewModel = ViewModelProvider(this, HomeViewModelProvider())[HomeViewModel::class.java]

        // Insert custom search suggestions mocks to the database
        viewModel.observeSuggestionsLiveData(viewLifecycleOwner) {
            if (it.isEmpty())
                viewModel.insertMocks()
        }

        // Obtaining navigation controller, used to navigate between fragments
        val navController = findNavController()

        // Programmes button
        button_home_programmes.setOnClickListener {
            navController.navigate(R.id.action_home_to_programmes)
        }

        // Calendar button
        button_home_calendar.setOnClickListener {
            navController.navigate(R.id.navigation_calendar)
        }


    }
}
