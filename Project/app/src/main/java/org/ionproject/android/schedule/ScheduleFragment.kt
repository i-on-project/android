package org.ionproject.android.schedule

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
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
    }

}
