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
import android.view.MenuItem
import android.view.View


class MovieDetailActivity : AppCompatActivity() {

    private lateinit var mediaViewModel: MovieDetailViewModel

    private val mMediaObserver = Observer<MediaEntity> { mediaEntity ->
        if (mediaEntity != null) {
            media_title.text = mediaEntity.title
            release_date.text = "Imdb : "+mediaEntity.imdbrating
            metascore.text = mediaEntity.metascore
            casting.text = mediaEntity.actors
            production.text = mediaEntity.production
            type.text = mediaEntity.runtime
            synopsis.text = mediaEntity.plot
            val mPoster = mediaEntity.poster
            if (mPoster!!.isNotEmpty() && mPoster != getString(R.string.na)) {
                Picasso.get().load(mPoster).into(media_image)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        val intent = intent
        val movieId = intent.getStringExtra(MEDIA_ID)
        val isBookMarked = intent.getBooleanExtra(BOOKMARK,false)



        mediaViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this, movieId))
                .get(MovieDetailViewModel::class.java)

        mediaViewModel.observableMedia?.let {
            observerMediaResult(it)
        }

        if(isBookMarked){
            bookmark.visibility = View.GONE
        }else{
            bookmark.visibility = View.VISIBLE
        }

        bookmark.setOnClickListener {
            mediaViewModel.bookMarkMovie(movieId)
            bookmark.visibility = View.GONE
        }
        setSupportActionBar(movie_detail_toolbar)
        supportActionBar?.title = getString(R.string.MovieDetail);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun observerMediaResult(mediaObservable: LiveData<MediaEntity>) {
        // Observer LiveData
        mediaObservable.observe(this, mMediaObserver)
    }

    companion object{
        const val BOOKMARK = "bookmark"
        const val MEDIA_ID = "mediaid"
    }
}
