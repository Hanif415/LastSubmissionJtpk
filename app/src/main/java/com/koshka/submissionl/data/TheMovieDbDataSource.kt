package com.koshka.submissionl.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.koshka.submissionl.data.local.entity.MovieEntity
import com.koshka.submissionl.vo.Resource

interface TheMovieDbDataSource {
    fun getAllMovies(): LiveData<Resource<PagedList<MovieEntity>>>
}
