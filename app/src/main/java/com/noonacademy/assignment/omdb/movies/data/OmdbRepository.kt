package com.noonacademy.assignment.omdb.movies.data

import androidx.paging.LivePagedListBuilder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.noonacademy.assignment.omdb.movies.api.OmdbService
import com.noonacademy.assignment.omdb.movies.api.fetchMovie
import com.noonacademy.assignment.omdb.movies.db.OmdbLocalCache
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import com.noonacademy.assignment.omdb.movies.model.MovieSearchResult

/**
 * Repository class that works with local and remote data sources.
 */
class OmdbRepository(
        private val service: OmdbService,
        private val cache: OmdbLocalCache
) {


    fun searchMovie(query: String): MovieSearchResult {
        Log.d("ODMRepository", "New query: $query")
        // Get data source factory from the local cache
        val dataSourceFactory = cache.moviesByName(query)

        // every new query creates a new BoundaryCallback
        // The BoundaryCallback will observe when the user reaches to the edges of
        // the list and update the database with extra data
        val boundaryCallback = MovieBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return MovieSearchResult(data, networkErrors)
    }

    fun fetchMovieDetails(query: String): LiveData<MediaEntity> {
        val moviesData = MutableLiveData<MediaEntity>()
        val networkErrors = MutableLiveData<String>()
        fetchMovie(service, query, { repos ->
            cache.insertMovie(repos) {
                moviesData.postValue(cache.moviesByMediaId(query))
            }
        }, { error ->
            networkErrors.postValue(error)
        })
        return moviesData
    }

    fun bookMarkMovie(mediaId:String){
        cache.updateMovieWithBookMark(mediaId)
    }


    companion object {
        private const val DATABASE_PAGE_SIZE = 20
    }
}
