package com.noonacademy.assignment.omdb.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import android.util.Log
import com.noonacademy.assignment.omdb.movies.api.OmdbService
import com.noonacademy.assignment.omdb.movies.api.fetchMoviesList
import com.noonacademy.assignment.omdb.movies.db.OmdbLocalCache
import com.noonacademy.assignment.omdb.movies.model.MediaEntity

/**
 * This boundary callback gets notified when user reaches to the edges of the list for example when
 * the database cannot provide any more data.Required for paging library
 **/
class MovieBoundaryCallback(
        private val query: String,
        private val service: OmdbService,
        private val cache: OmdbLocalCache
) : PagedList.BoundaryCallback<MediaEntity>() {

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

    // keep the last requested page. When the request is successful, increment the page number.
    private var lastRequestedPage = 1

    private val _networkErrors = MutableLiveData<String>()
    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    /**
     * Database returned 0 items. We should query the backend for more items.
     */
    override fun onZeroItemsLoaded() {
        Log.d("RepoBoundaryCallback", "onZeroItemsLoaded")
        requestMoviesAndSaveData(query)
    }

    /**
     * When all items in the database were loaded, we need to query the backend for more items.
     */
    override fun onItemAtEndLoaded(itemAtEnd: MediaEntity) {
        Log.d("RepoBoundaryCallback", "onItemAtEndLoaded")
        requestMoviesAndSaveData(query)
    }


    private fun requestMoviesAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        fetchMoviesList(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insertMedia(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }




}