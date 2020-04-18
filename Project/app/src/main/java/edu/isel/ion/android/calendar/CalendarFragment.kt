package edu.isel.ion.android.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.isel.ion.android.R

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.println(Log.DEBUG,"Calendar Fragment", "Is creating calendar fragment")

        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }
}