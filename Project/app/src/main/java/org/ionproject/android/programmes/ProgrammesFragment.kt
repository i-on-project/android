package org.ionproject.android.programmes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_programmes.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.SharedViewModelProvider

/**
 * A simple [Fragment] subclass.
 */
class ProgrammesFragment : Fragment() {

    /**
     * This view model is shared between fragments and the MainActivity
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelProvider()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_programmes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtaining view model
        val viewModel = ViewModelProviders
            .of(this, ProgrammesViewModelProvider())[ProgrammesViewModel::class.java]

        val adapter = ProgrammesListAdapter(viewModel, sharedViewModel)
        recyclerview_programmes_list.adapter = adapter
        recyclerview_programmes_list.layoutManager = LinearLayoutManager(context)
        viewModel.observeProgrammesLiveData(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }
    }

}
