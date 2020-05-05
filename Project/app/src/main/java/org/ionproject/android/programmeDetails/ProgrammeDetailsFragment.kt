package org.ionproject.android.programmeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_programme_details.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

class ProgrammeDetailsFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    private val programmeViewModel: ProgrammeDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(
            this,
            ProgrammeDetailsViewModelProvider()
        )[ProgrammeDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programme_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        programmeViewModel.getCourseDetails(sharedViewModel.programmeSummary) {
            textview_programme_details_name.text = it.name
            textview_programme_details_acronym.text = it.acronym
            recyclerview_programme_details_terms.layoutManager = LinearLayoutManager(context)
            recyclerview_programme_details_terms.adapter =
                TermsListAdapter(it.termSize, sharedViewModel)
            sharedViewModel.programme = it //Sharing programme with CoursesFragment
        }
    }


}
