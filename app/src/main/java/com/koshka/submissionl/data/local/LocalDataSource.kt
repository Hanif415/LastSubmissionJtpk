package com.koshka.submissionl.data.local

import androidx.paging.DataSource
import com.koshka.submissionl.data.local.entity.MovieEntity
import com.koshka.submissionl.data.local.room.TheMovieDbDao

class LocalDataSource private constructor(private val mTheMovieDbDao: TheMovieDbDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(mTheMovieDbDao: TheMovieDbDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(mTheMovieDbDao)
    }

    fun getAllMovie(): DataSource.Factory<Int, MovieEntity> = mTheMovieDbDao.getMovies()

    fun insertMovies(movieList: List<MovieEntity>) = mTheMovieDbDao.insertMovie(movieList)
}