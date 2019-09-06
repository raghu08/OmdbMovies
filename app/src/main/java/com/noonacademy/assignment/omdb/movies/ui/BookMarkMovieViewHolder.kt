package com.noonacademy.assignment.omdb.movies.ui

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.noonacademy.assignment.omdb.movies.R
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import com.squareup.picasso.Picasso

/**
 * View Holder for a [MediaEntity] RecyclerView list item.
 */
class BookMarkMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.media_title)
    private val mediaPoster: ImageView = view.findViewById(R.id.poster)
    val deleteBookMark: ImageView = view.findViewById(R.id.delete)
    private var media: MediaEntity? = null



    fun bind(movie: MediaEntity?) {
        if (movie == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showMovieData(movie)
        }
    }

    private fun showMovieData(media: MediaEntity) {
        this.media = media
        name.text = media.title
        Picasso.get().load(media.poster).into(mediaPoster)
    }

    companion object {
        fun create(parent: ViewGroup): BookMarkMovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.bookmark_movie_item, parent, false)
            return BookMarkMovieViewHolder(view)
        }
    }
}
