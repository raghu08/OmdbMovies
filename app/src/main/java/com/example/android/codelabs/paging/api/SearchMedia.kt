package com.example.android.codelabs.paging.api

import com.example.android.codelabs.paging.model.MediaEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchMedia {

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
