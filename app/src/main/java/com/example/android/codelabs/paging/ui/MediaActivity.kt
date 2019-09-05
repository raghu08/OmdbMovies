package com.example.android.codelabs.paging.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.codelabs.paging.Injection
import com.example.android.codelabs.paging.R
import com.example.android.codelabs.paging.model.MediaEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_media.*

class MediaActivity : AppCompatActivity() {

    private lateinit var mediaViewModel: MediaViewModel

    private val mMediaObserver = Observer<MediaEntity> { mediaEntity ->
        if (mediaEntity != null) {
          //  mBinding.mediaEntity = mediaEntity

            val mPoster = mediaEntity.poster
            if (!mPoster!!.isEmpty() && mPoster != "N/A") {
                Picasso.get().load(mPoster).into(media_image)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        val intent = intent
        val mMediaId = intent.getStringExtra("mediaid")

        mediaViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(MediaViewModel::class.java)

        observerMediaResult(mediaViewModel.observableMedia)

        btn_back.setOnClickListener {
            this.finish()
        }
        ic_rated.setOnClickListener{
            mediaViewModel.bookMarkMovie(mMediaId)
        }
    }

    private fun observerMediaResult(mediaObservable: LiveData<MediaEntity>) {
        // Observer LiveData
        mediaObservable.observe(this, mMediaObserver)
    }
}
