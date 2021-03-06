package org.ionproject.android.programmes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_programme.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.common.model.ProgrammeSummary

class ProgrammesListAdapter(
    private val model: ProgrammesViewModel,
    private val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<ProgrammesListAdapter.ProgrammeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_programme, parent, false)
        return ProgrammeViewHolder(view, sharedViewModel)
    }

    override fun getItemCount(): Int = model.programmeSummaries.count()

    override fun onBindViewHolder(holder: ProgrammeViewHolder, position: Int) {
        holder.bindTo(model.programmeSummaries[position])
    }

    class ProgrammeViewHolder(view: View, private val sharedViewModel: SharedViewModel) :
        RecyclerView.ViewHolder(view) {

        private val buttonProgramme = view.button_programmes_list_item_programme

        fun bindTo(programmeSummary: ProgrammeSummary) {
            buttonProgramme.text = programmeSummary.acronym
            buttonProgramme.setOnClickListener {
                sharedViewModel.programmeDetailsUri = programmeSummary.detailsUri
                it.findNavController().navigate(R.id.action_programmes_to_programme_details)
            }
        }
    }

}