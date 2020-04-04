package edu.isel.ion.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import edu.isel.ion.android.R

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

        //Obtaining view model
        val viewModel = ViewModelProviders
            .of(this, HomeViewModelProvider())[HomeViewModel::class.java]

        //Obtaining navigation controller, used to navigate between fragments
        val navController = findNavController()

        //Courses button
        view.findViewById<Button>(R.id.button_home_courses).setOnClickListener {
            navController.navigate(R.id.action_home_to_curricular_terms)
        }

        //Calendar button
        view.findViewById<Button>(R.id.button_home_calendar).setOnClickListener {
            TODO("Calendar fragment and navigation element")
        }
    }
}
