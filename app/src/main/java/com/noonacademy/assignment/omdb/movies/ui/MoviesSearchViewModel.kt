package com.noonacademy.assignment.omdb.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.noonacademy.assignment.omdb.movies.data.OmdbRepository
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import com.noonacademy.assignment.omdb.movies.model.MovieSearchResult

/**
 * ViewModel for the [SearchMoviesActivity] screen.
 * The ViewModel works with the [OmdbRepository] to get the data.
 */
class MoviesSearchViewModel(private val repository: OmdbRepository) : ViewModel() {

    private val queryLiveData = MutableLiveData<String>()
    var bookMarkLiveData = repository.getBookMarkedMovies()

    private val movieResult: LiveData<MovieSearchResult> = Transformations.map(queryLiveData) {
        repository.searchMovie(it)
    }

    val movies: LiveData<PagedList<MediaEntity>> = Transformations.switchMap(movieResult) { it.data }

    val networkErrors: LiveData<String> = Transformations.switchMap(movieResult) {
        it.networkErrors
    }

    /**
     * Search a repository based on a query string.
     */
    fun searchRepo(queryString: String) {
        queryLiveData.postValue(queryString)
    }

    fun getBookMarkedMovies() {
        repository.getBookMarkedMovies()
    }
    fun deleteMovie(mediaId:String) {
        repository.bookMarkMovie(mediaId,false)
        repository.getBookMarkedMovies()
    }

    /**
     * Get the last query value.
     */
    fun lastQueryValue(): String? = queryLiveData.value
}
