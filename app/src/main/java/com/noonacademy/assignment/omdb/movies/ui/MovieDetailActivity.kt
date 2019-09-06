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
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.layout_movie_detail_body.*
import kotlinx.android.synthetic.main.layout_movie_detail_header.*
import android.widget.Toolbar
import android.view.MenuItem


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var mediaViewModel: MovieDetailViewModel

    private val mMediaObserver = Observer<MediaEntity> { mediaEntity ->
        if (mediaEntity != null) {
            //  mBinding.mediaEntity = mediaEntity
            media_title.text = mediaEntity.title
            release_date.text = mediaEntity.released
            //   imdb_rated.text = mediaEntity.imdbrating
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
        setContentView(com.noonacademy.assignment.omdb.movies.R.layout.activity_movie_details)
        val intent = intent
        val movieId = intent.getStringExtra("mediaid")

        mediaViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this, movieId))
                .get(MovieDetailViewModel::class.java)

        mediaViewModel.observableMedia?.let {
            observerMediaResult(it)
        }

        ic_rated.setOnClickListener {
            mediaViewModel.bookMarkMovie(movieId)
        }
        setSupportActionBar(movie_detail_toolbar)
        supportActionBar?.title = "MovieDetail";
    }

   override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observerMediaResult(mediaObservable: LiveData<MediaEntity>) {
        // Observer LiveData
        mediaObservable.observe(this, mMediaObserver)
    }
}
