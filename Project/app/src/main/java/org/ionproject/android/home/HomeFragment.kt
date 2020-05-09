package org.ionproject.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import org.ionproject.android.R

class HomeFragment : Fragment() {

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

        // Courses button
        button_home_courses.setOnClickListener {
            //TODO: Swap navigation from courses to curricular terms
            navController.navigate(R.id.navigation_courses)
        }

        // Calendar button
        button_home_calendar.setOnClickListener {
            navController.navigate(R.id.action_home_to_calendar)
        }
    }
}
