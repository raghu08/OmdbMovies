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

package com.example.android.codelabs.paging.ui

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.android.codelabs.paging.R
import com.example.android.codelabs.paging.model.MediaEntity
import com.example.android.codelabs.paging.model.Repo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.media_item.view.*

/**
 * View Holder for a [Repo] RecyclerView list item.
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
                val intent = Intent(view.context,MediaActivity::class.java)
                intent.putExtra("mediaid",it)
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
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: MediaEntity) {
        this.repo = repo
        name.text = repo.title
        mediaType.text = repo.type
        mediaYear.text = repo.year
        Picasso.get().load(repo.poster).into(mediaPoster)
    }

    companion object {
        fun create(parent: ViewGroup): SearchMovieViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.media_item, parent, false)
            return SearchMovieViewHolder(view)
        }
    }
}
