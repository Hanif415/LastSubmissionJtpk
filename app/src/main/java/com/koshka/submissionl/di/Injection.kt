package com.koshka.submissionl.di

import android.content.Context
import com.koshka.submissionl.data.TheMovieDBRepository
import com.koshka.submissionl.data.local.LocalDataSource
import com.koshka.submissionl.data.local.room.TheMovieDbDatabase
import com.koshka.submissionl.data.remote.RemoteDataSource
import com.koshka.submissionl.utils.AppExecutors

// create the repository instance
object Injection {
    fun provideRepository(context: Context): TheMovieDBRepository {

        val database = TheMovieDbDatabase.getInstance(context)
        val remoteDataSource = RemoteDataSource.getInstance()

        val localDataSource = LocalDataSource.getInstance(database.theMovieDbDao())

        val appExecutor = AppExecutors()

        return TheMovieDBRepository(remoteDataSource, localDataSource, appExecutor)
    }
}
