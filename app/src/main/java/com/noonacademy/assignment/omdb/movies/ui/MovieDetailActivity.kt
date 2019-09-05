package com.noonacademy.assignment.omdb.movies.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.noonacademy.assignment.omdb.movies.Injection
import com.noonacademy.assignment.omdb.movies.R
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var mediaViewModel: MovieDetailViewModel

    private val mMediaObserver = Observer<MediaEntity> { mediaEntity ->
        if (mediaEntity != null) {
          //  mBinding.mediaEntity = mediaEntity
            media_title.text = mediaEntity.title
            release_date.text = mediaEntity.released
            imdb_rated.text = mediaEntity.imdbrating
            metascore.text = mediaEntity.metascore
            casting.text = mediaEntity.actors
            production.text = mediaEntity.production
            type.text = mediaEntity.type
            synopsis.text = mediaEntity.boxoffice
            val mPoster = mediaEntity.poster
            if (!mPoster!!.isEmpty() && mPoster != "N/A") {
                Picasso.get().load(mPoster).into(media_image)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        val intent = intent
        val mMediaId = intent.getStringExtra("mediaid")

        mediaViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this,mMediaId))
                .get(MovieDetailViewModel::class.java)

        mediaViewModel.observableMedia?.let {
            observerMediaResult(it) }

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
