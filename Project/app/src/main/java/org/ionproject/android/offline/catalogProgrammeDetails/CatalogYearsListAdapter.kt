package org.ionproject.android.offline.catalogProgrammeDetails

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_programme_details_list_item.view.*
import org.ionproject.android.R
import org.ionproject.android.offline.CatalogSharedViewModel
import org.ionproject.android.offline.models.CatalogAcademicYear

class CatalogYearsListAdapter(
    private val model: CatalogProgrammeDetailsViewModel,
    private val sharedViewModel: CatalogSharedViewModel
) : RecyclerView.Adapter<CatalogYearsListAdapter.CatalogYearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogYearViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.catalog_programme_details_list_item, parent, false)
        return CatalogYearViewHolder(view, sharedViewModel)
    }

    override fun getItemCount(): Int = model.catalogAcademicYears.size

    override fun onBindViewHolder(holder: CatalogYearViewHolder, position: Int) {
        holder.bind(model.catalogAcademicYears[position])
    }

    class CatalogYearViewHolder(val view: View, private val sharedViewModel: CatalogSharedViewModel) :

        RecyclerView.ViewHolder(view) {

        private val yearTextView = view.catalog_exam_name_textView

        private val firstSemesterButton = view.button_catalog_programme_details_first_term
        private val secondSemesterButton = view.button_catalog_programme_details_second_term

        fun bind(academicYear: CatalogAcademicYear) {

            Log.d("Catalog", "the year is ${academicYear.year}")

            yearTextView.text = academicYear.year

            firstSemesterButton.text = view.resources.getString(R.string.first_semester)
            firstSemesterButton.setOnClickListener {
                sharedViewModel.selectedYear = academicYear.year
                sharedViewModel.selectedCatalogProgrammeTerm = academicYear.year + "-1"
                view.findNavController().navigate(R.id.action_catalog_programme_details_to_catalogTermFilesFragment)
            }

            secondSemesterButton.text = view.resources.getString(R.string.second_semester)
            secondSemesterButton.setOnClickListener {
                sharedViewModel.selectedYear = academicYear.year
                sharedViewModel.selectedCatalogProgrammeTerm = academicYear.year + "-2"
                view.findNavController().navigate(R.id.action_catalog_programme_details_to_catalogTermFilesFragment)
            }
        }
    }
}