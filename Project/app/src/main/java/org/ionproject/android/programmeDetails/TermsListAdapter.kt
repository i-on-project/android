package org.ionproject.android.programmeDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_curricular_terms.view.*
import org.ionproject.android.R
import org.ionproject.android.SharedViewModel

class TermsListAdapter(private val terms: Int, private val sharedViewModel: SharedViewModel) :
    RecyclerView.Adapter<TermsListAdapter.TermViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TermViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_curricular_terms, parent, false)
        return TermViewHolder(view, sharedViewModel)
    }

    override fun getItemCount(): Int = terms

    override fun onBindViewHolder(holder: TermViewHolder, position: Int) {
        //Terms start in 1 and because there is no list we use the position directly
        holder.bind(position + 1)
    }

    class TermViewHolder(private val view: View, private val sharedViewModel: SharedViewModel) :
        RecyclerView.ViewHolder(view) {

        val button = view.button_terms_list_item_term

        fun bind(term: Int) {
            button.text = view.resources.getString(
                R.string.placeholder_curricular_term_programme_details,
                term.toString()
            )

            button.setOnClickListener {
                sharedViewModel.curricularTerm = term
                view.findNavController().navigate(R.id.action_programme_details_to_courses)
            }
        }

    }

}