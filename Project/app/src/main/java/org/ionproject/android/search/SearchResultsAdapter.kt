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
        holder.bindTo(searchResultsViewModel.searchResults[position])

    class SearchResultsViewHolder(
        view: View,
        private val onClickListener: (SearchResult) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val searchResultButton = view.button_search_list_item_searchResult

        fun bindTo(searchResult: SearchResult) {
            searchResultButton.text = searchResult.properties["name"]
            searchResultButton.setOnClickListener {
                onClickListener(searchResult)
            }
        }
    }
}