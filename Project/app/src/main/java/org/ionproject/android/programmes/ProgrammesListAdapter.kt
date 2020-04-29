package org.ionproject.android.programmes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ionproject.android.R
import org.ionproject.android.common.model.Programme

class ProgrammesListAdapter(private val model : ProgrammesViewModel) : RecyclerView.Adapter<ProgrammesListAdapter.ProgrammeViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgrammeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_programme,parent,false)
        return ProgrammeViewHolder(view)
    }

    override fun getItemCount(): Int = model.programmes.count()

    override fun onBindViewHolder(holder: ProgrammeViewHolder, position: Int) {
        holder.bindTo(model.programmes[position])
    }

    class ProgrammeViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        fun bindTo(programme: Programme) {

        }

    }

}