package com.koshka.submissionl.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.koshka.submissionl.data.TheMovieDBRepository
import com.koshka.submissionl.data.local.entity.MovieEntity
import com.koshka.submissionl.vo.Resource

class MovieViewModel(private val theMovieDBRepository: TheMovieDBRepository) : ViewModel() {
    fun getMovies(): LiveData<Resource<PagedList<MovieEntity>>> = theMovieDBRepository.getAllMovies()
}