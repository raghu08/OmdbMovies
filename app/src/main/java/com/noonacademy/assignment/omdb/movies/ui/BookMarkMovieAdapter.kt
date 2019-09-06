package com.noonacademy.assignment.omdb.movies.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.noonacademy.assignment.omdb.movies.model.MediaEntity

/**
 * Adapter for the list of repositories.
 */
class BookMarkMovieAdapter(private var items : List<MediaEntity>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int =items.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BookMarkMovieViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as BookMarkMovieViewHolder).bind(repoItem)
        }
    }

    fun notifyDataSet(items : List<MediaEntity>){
        this.items = items
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): MediaEntity? = items[position]


}
