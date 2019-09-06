package com.noonacademy.assignment.omdb.movies.ui

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.noonacademy.assignment.omdb.movies.model.MediaEntity

/**
 * Adapter for the list of repositories.
 */
class SearchMovieAdapter : PagedListAdapter<MediaEntity, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchMovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as SearchMovieViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<MediaEntity>() {
            override fun areItemsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean =
                    oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MediaEntity, newItem: MediaEntity): Boolean =
                    oldItem == newItem
        }
    }
}
