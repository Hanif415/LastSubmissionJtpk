package com.koshka.submissionl.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koshka.submissionl.data.TheMovieDBRepository
import com.koshka.submissionl.di.Injection
import com.koshka.submissionl.ui.movie.MovieViewModel

class ViewModelFactory private constructor(private val mMovieRepository: TheMovieDBRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            ViewModelFactory(Injection.provideRepository(context)).apply { instance = this }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(mMovieRepository) as T
            }
            else -> throw Throwable("Unknown View Model Class: " + modelClass.name)
        }
    }

}