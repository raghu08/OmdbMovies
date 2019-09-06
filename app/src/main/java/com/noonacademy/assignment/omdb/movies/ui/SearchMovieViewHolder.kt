package com.noonacademy.assignment.omdb.movies.ui

import android.content.Intent
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
class SearchMovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val name: TextView = view.findViewById(R.id.media_title)
    private val mediaType: TextView = view.findViewById(R.id.media_type)
    private val mediaPoster: ImageView = view.findViewById(R.id.media_image)
    private val mediaYear: TextView = view.findViewById(R.id.media_year)

    private var repo: MediaEntity? = null

    init {
        view.setOnClickListener {
            repo?.imdbid?.let {
                val intent = Intent(view.context, MovieDetailActivity::class.java)
                intent.putExtra(MovieDetailActivity.MEDIA_ID, it)
                intent.putExtra(MovieDetailActivity.BOOKMARK, repo?.bookmark)
                view.context.startActivity(intent)
            }
        }
    }

    fun bind(repo: MediaEntity?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            mediaType.visibility = View.GONE
        } else {
            showMovieData(repo)
        }
    }

    private fun showMovieData(repo: MediaEntity) {
        this.repo = repo
        name.text = repo.title
        mediaType.text = repo.type
        mediaYear.text = repo.year
        Picasso.get().load(repo.poster).into(mediaPoster)
    }

    companion object {
        fun create(parent: ViewGroup): SearchMovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.movie_item_row, parent, false)
            return SearchMovieViewHolder(view)
        }
    }
}
