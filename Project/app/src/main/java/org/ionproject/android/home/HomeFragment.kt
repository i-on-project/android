package org.ionproject.android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        // Obtaining navigation controller, used to navigate between fragments
        val navController = findNavController()

        // Programmes button
        button_home_programmes.setOnClickListener {
            navController.navigate(R.id.action_home_to_programmes)
        }

        // Calendar button
        button_home_calendar.setOnClickListener {
            navController.navigate(R.id.action_home_to_calendar)
        }

        // Info button
        button_home_info.setOnClickListener {
            navController.navigate(R.id.action_home_to_info)
        }

        // Settings button
        button_home_settings.setOnClickListener {
            navController.navigate(R.id.action_home_to_settings)
        }
    }
}
