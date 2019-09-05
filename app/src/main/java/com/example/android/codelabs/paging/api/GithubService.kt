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

package com.example.android.codelabs.paging.api

import android.util.Log
import com.example.android.codelabs.paging.model.MediaEntity
import com.example.android.codelabs.paging.model.Repo
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

private const val TAG = "GithubService"
private const val IN_QUALIFIER = "in:name,description"

/**
 * Search repos based on a query.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param query searchRepo keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun searchRepos(
    service: GithubService,
    query: String,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (repos: List<Repo>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

    val apiQuery = query + IN_QUALIFIER

    service.searchRepos(apiQuery, page, itemsPerPage).enqueue(
            object : Callback<RepoSearchResponse> {
                override fun onFailure(call: Call<RepoSearchResponse>?, t: Throwable) {
                    Log.d(TAG, "fail to get dataa")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                    call: Call<RepoSearchResponse>?,
                    response: Response<RepoSearchResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val repos = response.body()?.items ?: emptyList()
                        onSuccess(repos)
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

fun fetchMediasList(service: GithubService,query: String,page: Int,itemsPerPage: Int,onSuccess: (repos: List<MediaEntity>) -> Unit,
                            onError: (error: String) -> Unit) {

    service.searchMediaListFromTitle(query,page).enqueue(object : Callback<SearchMedia> {
        override fun onResponse(call: Call<SearchMedia>, response: Response<SearchMedia>?) {
            response?.let {
                if (it.isSuccessful && it.code() == 200) {
                    val repos = response.body()?.mMediaEntityList ?: emptyList()
                    onSuccess(repos)
                } else {
                    Log.e("API CALL", response.message())
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }

        override fun onFailure(call: Call<SearchMedia>, t: Throwable) {
            if (!call.isCanceled) {
                Log.e("API CALL FAILED", t.message)
                onError(t.message ?: "unknown error")
            }
        }
    })
}

/**
 * Github API communication setup via Retrofit.
 */
interface GithubService {
    /**
     * Get repos ordered by stars.
     */
    @GET("search/repositories?sort=stars")
    fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<RepoSearchResponse>

    @GET(".")
    fun searchMediaListFromTitle(@Query(value = "s", encoded = true) search: String, @Query("page") page: Int):
            Call<SearchMedia>


    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val BASE_URL_MOVIES = "http://www.omdbapi.com/"

        fun create(): GithubService {
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
                    .create(GithubService::class.java)
        }
    }
}