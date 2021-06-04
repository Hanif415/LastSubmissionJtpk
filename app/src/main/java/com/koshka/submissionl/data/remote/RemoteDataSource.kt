package com.koshka.submissionl.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.koshka.submissionl.data.remote.response.ApiResponse
import com.koshka.submissionl.data.remote.response.MovieResponse
import com.koshka.submissionl.service.MovieApi
import com.koshka.submissionl.utils.EspressoIdlingResource

class RemoteDataSource {

    companion object {

        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                RemoteDataSource().apply { instance = this }
            }
    }

    fun getAllMovie(): LiveData<ApiResponse<List<MovieResponse>>> {
        EspressoIdlingResource.increment()

        val resultMovie = MutableLiveData<ApiResponse<List<MovieResponse>>>()

        resultMovie.value = ApiResponse.success(MovieApi.movieApiService.getMovies())

        EspressoIdlingResource.decrement()

        return resultMovie
    }
}