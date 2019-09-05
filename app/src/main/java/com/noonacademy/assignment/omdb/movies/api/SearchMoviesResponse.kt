package com.noonacademy.assignment.omdb.movies.api

import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchMoviesResponse {

    @SerializedName("Search")
    @Expose
    var mMediaEntityList: List<MediaEntity>? = null

    @SerializedName("totalResults")
    @Expose
    var totalResults: String? = null

    @SerializedName("Response")
    @Expose
    var response: String? = null
}
