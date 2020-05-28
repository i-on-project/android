package org.ionproject.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider
import org.ionproject.android.common.dto.ResourceType

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
        val viewModel = ViewModelProviders
            .of(this, HomeViewModelProvider())[HomeViewModel::class.java]

        // Insert custom search suggestions mocks to the database
        viewModel.observeSuggestionsLiveData(viewLifecycleOwner) {
            if (it.isEmpty())
                viewModel.insertMocks()
        }

        // Obtaining navigation controller, used to navigate between fragments
        val navController = findNavController()

        // Hide programmes button until we are sure json home has the programmes resource
        button_home_programmes.visibility = View.GONE
        sharedViewModel.observeJsonHomeLiveData(this) { jsonHome ->

            // Only add programmes button if Json Home contains that resource
            if (jsonHome.getResourceByType(ResourceType.PROGRAMMES) != null) {
                button_home_programmes.visibility = View.VISIBLE
                // programmes
                button_home_programmes.setOnClickListener {
                    navController.navigate(R.id.action_home_to_programmes)
                }
            } else {
                button_home_programmes.visibility = View.GONE
            }

        }

        // Calendar button
        button_home_calendar.setOnClickListener {
            TODO("Calendar fragment and navigation element")
        }


    }
}
