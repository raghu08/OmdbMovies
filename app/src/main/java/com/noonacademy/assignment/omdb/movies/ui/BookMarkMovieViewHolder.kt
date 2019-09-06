/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private val deleteBookMark: ImageView = view.findViewById(R.id.delete)

    private var media: MediaEntity? = null



    fun bind(repo: MediaEntity?) {
        deleteBookMark.setOnClickListener{

        }
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showMovieData(repo)
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
                    .inflate(R.layout.view_pager_item, parent, false)
            return BookMarkMovieViewHolder(view)
        }
    }
}
