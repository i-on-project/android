package org.ionproject.android.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_search_result.view.*
import org.ionproject.android.R
import org.ionproject.android.common.model.SearchResult

class SearchResultsAdapter(
    private val searchResultsViewModel: SearchResultsViewModel,
    private val onClickListener: (SearchResult) -> Unit
) :
    PagedListAdapter<SearchResult, SearchResultsAdapter.SearchResultsViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
                oldItem.resourceURI == newItem.resourceURI

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_search_result, parent, false)

        return SearchResultsViewHolder(view, onClickListener)
    }

    override fun onBindViewHolder(holder: SearchResultsViewHolder, position: Int) =
        getItem(position).let { if (it != null) holder.bindTo(it) }

    class SearchResultsViewHolder(
        view: View,
        private val onClickListener: (SearchResult) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val searchResultCardView = view.materialcardview_search_result
        private val searchResultTypeTextView = view.textview_search_result_type
        private val searchResultTextTextView = view.textview_search_result_text

        fun bindTo(searchResult: SearchResult) {
            searchResultTypeTextView.text =
                searchResult.type.getNameFromResource(itemView.resources)
            searchResultTextTextView.text = searchResult.properties["name"]
            searchResultCardView.setOnClickListener {
                onClickListener(searchResult)
            }
        }
    }
}