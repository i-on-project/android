package org.ionproject.android.offline

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_curricular_terms.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel
import org.ionproject.android.offline.models.CatalogTerm
import org.ionproject.android.programmeDetails.ProgrammeDetailsViewModel
import java.net.URI

class CatalogTermsListAdapter(
    private val model: ProgrammeDetailsViewModel,
    private val sharedViewModel: SharedViewModel
) : RecyclerView.Adapter<CatalogTermsListAdapter.CatalogTermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogTermViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_curricular_terms, parent, false)
        return CatalogTermViewHolder(view, sharedViewModel)
    }

    override fun getItemCount(): Int = model.catalogProgrammeTerms.size

    override fun onBindViewHolder(holder: CatalogTermViewHolder, position: Int) {
        holder.bind(model.catalogProgrammeTerms[position])
    }

    /**
     * Since we can display both the timetable and the exam schedule in the class section fragment,
     * we don't need to go through all the other fragments in between that are necessary in the
     * normal usage
     */
    class CatalogTermViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) :

        RecyclerView.ViewHolder(view) {

        val button = view.button_terms_list_item_term

        fun bind(catalogTerm: CatalogTerm) {
            button.text = catalogTerm.term

            button.setOnClickListener {
                sharedViewModel.selectedCatalogProgrammeTerm = catalogTerm
                view.findNavController().navigate(R.id.action_navigation_programme_details_to_navigation_course_details)
            }
        }
    }
}