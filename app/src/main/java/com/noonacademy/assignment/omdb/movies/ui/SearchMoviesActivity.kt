package com.noonacademy.assignment.omdb.movies.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import com.noonacademy.assignment.omdb.movies.Injection
import com.noonacademy.assignment.omdb.movies.R
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import kotlinx.android.synthetic.main.activity_search_movies.*
import androidx.recyclerview.widget.LinearLayoutManager


class SearchMoviesActivity : AppCompatActivity() {

    private lateinit var viewModel: MoviesSearchViewModel
    private val adapterMovies = SearchMovieAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_movies)

        // get the view model
        viewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this))
                .get(MoviesSearchViewModel::class.java)

        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        movie_list.addItemDecoration(decoration)

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        viewModel.searchRepo(query)

        initSearch(query)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, viewModel.lastQueryValue())
    }

    private fun initAdapter() {
        movie_list.adapter = adapterMovies
        val bookMarkAdapter = BookMarkMovieAdapter(listOf(),viewModel)
        bookmarkedMovies.adapter = bookMarkAdapter
        bookmarkedMovies.layoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.HORIZONTAL, false)
        viewModel.movies.observe(this, Observer<PagedList<MediaEntity>> {
            showEmptyList(it?.size == 0)
            adapterMovies.submitList(it)
        })
        viewModel.getBookMarkedMovies()
        viewModel.bookMarkLiveData.observe(this, Observer<List<MediaEntity>?> {
            showOrHideBookmarks(it?.size == 0)
            it?.let { bookMarkAdapter.notifyDataSet(it) }
        })
        viewModel.networkErrors.observe(this, Observer<String> {
            Toast.makeText(this, "\uD83D\uDE28 Wooops $it", Toast.LENGTH_LONG).show()
        })
    }

    private fun initSearch(query: String) {
        search_movies.setText(query)

        search_movies.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
        search_movies.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateRepoListFromInput() {
        search_movies.text.trim().let {
            if (it.isNotEmpty()) {
                movie_list.scrollToPosition(0)
                viewModel.searchRepo(it.toString())
                adapterMovies.submitList(null)
            }
        }
    }

    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visibility = View.VISIBLE
            movie_list.visibility = View.GONE
        } else {
            emptyList.visibility = View.GONE
            movie_list.visibility = View.VISIBLE
        }
    }

    private fun showOrHideBookmarks(show: Boolean) {
        if (show) {
            bookmarkedMovies.visibility = View.GONE
        } else {
            bookmarkedMovies.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Jack"
    }
}
