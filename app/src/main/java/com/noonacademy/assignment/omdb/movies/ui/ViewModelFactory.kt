package com.noonacademy.assignment.omdb.movies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noonacademy.assignment.omdb.movies.data.OmdbRepository

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val repository: OmdbRepository, private val  mediaId:String?=null) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoviesSearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesSearchViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailViewModel(repository,mediaId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
