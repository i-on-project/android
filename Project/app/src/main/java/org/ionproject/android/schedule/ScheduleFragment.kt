package org.ionproject.android.schedule

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_schedule.view.*
import org.ionproject.android.MainActivity
import org.ionproject.android.R

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.println(Log.DEBUG, "Schedule Fragment", "Is creating schedule fragment")

        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.bottomnavview_main?.visibility = View.GONE
        (activity as MainActivity)?.supportActionBar?.hide()
        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
        view.button_schedule_back.setOnClickListener {
            activity?.bottomnavview_main?.visibility = View.VISIBLE
            (activity as MainActivity)?.supportActionBar?.show()
            activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            findNavController().navigateUp()
        }
    }

}
