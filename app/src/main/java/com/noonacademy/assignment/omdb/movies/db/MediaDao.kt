package com.noonacademy.assignment.omdb.movies.db

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.noonacademy.assignment.omdb.movies.model.MediaEntity

@Dao
interface MediaDao {

    @Query("SELECT * FROM media")
    fun loadAllMedia(): LiveData<List<MediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mediaEntityList: List<MediaEntity>)

    @Query("select * from media where imdbid = :mediaId")
    fun loadMedia(mediaId: String): MediaEntity

    @Query("select * from media where bookmark = :isBookmarked")
    fun loadBookMarkedMedia(isBookmarked:Boolean): List<MediaEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(mediaEntity: MediaEntity)

    @Query("UPDATE media SET bookmark=:bookmarked WHERE imdbid = :mediaId")
    fun update(bookmarked: Boolean, mediaId: String)

    @Query("DELETE FROM media WHERE imdbid = :mediaId")
    fun deleteMovie(mediaId: String)

    @Query("select * from media where title like '%' || :search || '%'")
    fun loadMediaFromSearch(search: String):DataSource.Factory<Int, MediaEntity>
}
