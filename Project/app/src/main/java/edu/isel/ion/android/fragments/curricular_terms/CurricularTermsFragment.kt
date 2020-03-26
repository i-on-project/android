package edu.isel.ion.android.fragments.curricular_terms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.isel.ion.android.R

/**
 * A simple [Fragment] subclass.
 */
class CurricularTermsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curricular_terms, container, false)
    }

}
