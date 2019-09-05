package com.example.android.codelabs.paging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.codelabs.paging.data.GithubRepository
import com.example.android.codelabs.paging.model.MediaEntity

class MediaViewModel private constructor(val repository: GithubRepository, mediaId: String) : ViewModel() {


    val observableMedia: LiveData<MediaEntity> = repository.loadMedia(mediaId)

    fun bookMarkMovie(mediaId: String) {
     //   repository.(mediaId)
    }



}