package org.ionproject.android.offline.catalogProgrammes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_programme.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.models.CatalogProgramme

class CatalogProgrammesListAdapter(
    private val model: CatalogProgrammesViewModel,
    private val sharedViewModel: CatalogSharedViewModel
) :
    RecyclerView.Adapter<CatalogProgrammesListAdapter.CatalogProgrammeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogProgrammeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_programme, parent, false)
        return CatalogProgrammeViewHolder(view, sharedViewModel)
    }

    override fun getItemCount(): Int = model.catalogProgrammes.size

    override fun onBindViewHolder(holder: CatalogProgrammeViewHolder, position: Int) {
        holder.bindTo(model.catalogProgrammes[position])
    }

    class CatalogProgrammeViewHolder(
        view: View,
        private val sharedViewModel: CatalogSharedViewModel
    ) :
        RecyclerView.ViewHolder(view) {

        private val buttonProgramme = view.button_programmes_list_item_programme

        fun bindTo(catalogProgramme: CatalogProgramme) {
            buttonProgramme.text = catalogProgramme.programmeName

            buttonProgramme.setOnClickListener {
                sharedViewModel.selectedCatalogProgramme = catalogProgramme
                it.findNavController()
                    .navigate(R.id.action_catalog_programmes_to_catalogProgrammeDetailsFragment)
            }
        }
    }
}