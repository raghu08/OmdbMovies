package com.noonacademy.assignment.omdb.movies.api

import android.util.Log
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Search Movies based on a query.
 * @param query movie title/Tvshow keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Omdb API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */

const val TAG = "API CALL"
const val ERROR_MSG = "unknown error"

fun fetchMoviesList(service: OmdbService, query: String, page: Int, itemsPerPage: Int, onSuccess: (repos: List<MediaEntity>) -> Unit,
                    onError: (error: String) -> Unit) {

    service.searchMovies(query, page).enqueue(object : Callback<SearchMoviesResponse> {
        override fun onResponse(call: Call<SearchMoviesResponse>, response: Response<SearchMoviesResponse>?) {
            response?.let {
                if (it.isSuccessful && it.code() == 200) {
                    val repos = response.body()?.mMediaEntityList ?: emptyList()
                    onSuccess(repos)
                } else {
                    Log.e(TAG, response.message())
                    onError(response.errorBody()?.string() ?: ERROR_MSG)
                }
            }
        }

        override fun onFailure(call: Call<SearchMoviesResponse>, t: Throwable) {
            if (!call.isCanceled) {
                Log.e(TAG, t.message)
                onError(t.message ?: ERROR_MSG)
            }
        }
    })
}

fun fetchMovie(service: OmdbService, query: String, onSuccess: (repos: MediaEntity) -> Unit,
               onError: (error: String) -> Unit) {

    service.searchMovieById(query, "full").enqueue(object : Callback<MediaEntity> {
        override fun onResponse(call: Call<MediaEntity>, response: Response<MediaEntity>?) {
            response?.let {
                if (it.isSuccessful && it.code() == 200) {
                    it.body()?.let {
                        onSuccess(it)
                    }
                } else {
                    Log.e(TAG, response.message())
                }
            }
        }

        override fun onFailure(call: Call<MediaEntity>, t: Throwable) {
            if (!call.isCanceled) {
                Log.e(TAG, t.message)
                onError(t.message ?: ERROR_MSG)
            }
        }
    })
}

/**
 * Omdb API communication setup via Retrofit.
 */
interface OmdbService {
    @GET(".")
    fun searchMovies(@Query(value = "s", encoded = true) search: String, @Query("page") page: Int):
            Call<SearchMoviesResponse>

    @GET(".")
    fun searchMovieById(@Query("i") imdbId: String, @Query("plot") plot: String): Call<MediaEntity>


    companion object {
        private const val BASE_URL_MOVIES = "http://www.omdbapi.com/"
        fun create(): OmdbService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC
            val client = OkHttpClient.Builder()
                    .addInterceptor(CurlLoggingInterceptor())
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL_MOVIES)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(OmdbService::class.java)
        }
    }
}