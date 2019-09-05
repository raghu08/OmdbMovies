package com.noonacademy.assignment.omdb.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.noonacademy.assignment.omdb.movies.data.OmdbRepository
import com.noonacademy.assignment.omdb.movies.model.MediaEntity

class MovieDetailViewModel(private val repository: OmdbRepository, mediaId: String?) : ViewModel() {


    val observableMedia: LiveData<MediaEntity>? = mediaId?.let {
        repository.fetchMovieDetails(it)
    }

    fun bookMarkMovie(mediaId: String) {
        repository.bookMarkMovie(mediaId)
    }



}