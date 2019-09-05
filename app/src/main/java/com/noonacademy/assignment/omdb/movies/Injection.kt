package com.noonacademy.assignment.omdb.movies

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.noonacademy.assignment.omdb.movies.api.OmdbService
import com.noonacademy.assignment.omdb.movies.data.OmdbRepository
import com.noonacademy.assignment.omdb.movies.db.OmdbLocalCache
import com.noonacademy.assignment.omdb.movies.db.OmdbDatabase
import com.noonacademy.assignment.omdb.movies.ui.ViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [OmdbLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): OmdbLocalCache {
        val database = OmdbDatabase.getInstance(context)
        return OmdbLocalCache(database.mediaDao(),Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [OmdbRepository] based on the [OmdbService] and a
     * [OmdbLocalCache]
     */
    private fun provideOmdbRepository(context: Context): OmdbRepository {
        return OmdbRepository(OmdbService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context,queryId:String?=null): ViewModelProvider.Factory {
        return ViewModelFactory(provideOmdbRepository(context),queryId)
    }
}
