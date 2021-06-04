package com.koshka.submissionl.data.local.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.koshka.submissionl.data.local.entity.MovieEntity

@Dao
interface TheMovieDbDao {
    @Query("SELECT * FROM movieEntities")
    fun getMovies(): DataSource.Factory<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: List<MovieEntity>)
}