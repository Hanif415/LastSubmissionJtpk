package com.koshka.submissionl.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.koshka.submissionl.data.local.LocalDataSource
import com.koshka.submissionl.data.local.entity.MovieEntity
import com.koshka.submissionl.data.remote.RemoteDataSource
import com.koshka.submissionl.data.remote.response.ApiResponse
import com.koshka.submissionl.data.remote.response.MovieResponse
import com.koshka.submissionl.utils.AppExecutors
import com.koshka.submissionl.vo.Resource

class TheMovieDBRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : TheMovieDbDataSource {

    override fun getAllMovies(): LiveData<Resource<PagedList<MovieEntity>>> {

        return object :
            NetworkBoundResource<PagedList<MovieEntity>, List<MovieResponse>>(appExecutors) {

            override fun loadFromDb(): LiveData<PagedList<MovieEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()

                return LivePagedListBuilder(localDataSource.getAllMovie(), config).build()
            }

            override fun shouldFetch(data: PagedList<MovieEntity>?): Boolean =
                data == null || data.isEmpty()

            // get movie by API
            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getAllMovie()

            override fun saveCallResult(movieResponse: List<MovieResponse>?) {
                val movieList = ArrayList<MovieEntity>()

                for (response in movieResponse!!) {
                    val movie = MovieEntity(
                        response.id!!,
                        response.title!!,
                        response.overview!!,
                        response.posterPath!!,
                        response.releaseDate!!,
                        response.voteAverage!!
                    )

                    movieList.add(movie)
                }

                localDataSource.insertMovies(movieList)
            }

        }.asLiveData()
    }
}