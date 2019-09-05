
package com.noonacademy.assignment.omdb.movies.db

import android.util.Log
import androidx.paging.DataSource
import com.noonacademy.assignment.omdb.movies.model.MediaEntity
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class OmdbLocalCache(
        private val mediaDao: MediaDao,
        private val ioExecutor: Executor
) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insertMedia(repos: List<MediaEntity>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("OmdbLocalCache", "inserting ${repos.size} repos")
            mediaDao.insertAll(repos)
            insertFinished()
        }
    }

    /**
     * Insert a single record in the database, on a background thread.
     */
    fun insertMovie(repos: MediaEntity, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("OmdbLocalCache", "inserting ${repos.title} repos")
            mediaDao.insertMedia(repos)
            insertFinished()
        }
    }

    /**
     * Insert a single record in the database, on a background thread.
     */
    fun updateMovieWithBookMark(query: String) {
        ioExecutor.execute {
            Log.d("OmdbLocalCache", "inserting ${query} repos")
            mediaDao.update(true, query)
        }
    }


    fun moviesByName(name: String): DataSource.Factory<Int, MediaEntity> {
        // appending '%' so we can allow other characters to be before and after the query string
        //val query = "%${name.replace(' ', '%')}%"
        return mediaDao.loadMediaFromSearch(name)
    }

    fun moviesByMediaId(name: String): MediaEntity {
        return mediaDao.loadMedia(name)
    }
}
