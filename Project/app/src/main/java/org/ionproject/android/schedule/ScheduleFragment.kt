package org.ionproject.android.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.ionproject.android.R

class ScheduleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.println(Log.DEBUG, "Schedule Fragment", "Is creating schedule fragment")

        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

}
