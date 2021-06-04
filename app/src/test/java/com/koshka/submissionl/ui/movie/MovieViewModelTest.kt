package com.koshka.submissionl.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.koshka.submissionl.data.TheMovieDBRepository
import com.koshka.submissionl.data.local.entity.MovieEntity
import com.koshka.submissionl.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    // we use instant executor rule because we use asynchronous
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // repository
    @Mock
    private lateinit var theMovieDBRepository: TheMovieDBRepository

    // observer
    @Mock
    private lateinit var observer: Observer<Resource<PagedList<MovieEntity>>>

    @Before
    fun setUp() {
        viewModel = MovieViewModel(theMovieDBRepository)
    }

    // paged List
    @Mock
    private lateinit var pagedList: PagedList<MovieEntity>

    @Test
    fun getSource() {
        // dummy data
        val dummyMovies = Resource.success(pagedList)
        `when`(dummyMovies.data?.size).thenReturn(5)

        // Movie Entity
        val course = MutableLiveData<Resource<PagedList<MovieEntity>>>()
        // add movie entity with dummy data
        course.value = dummyMovies

        `when`(theMovieDBRepository.getAllMovies()).thenReturn(course)

        // get the value of getMovie()
        val movieEntities = viewModel.getMovies().value?.data

        // verify the repository is run getAllMovies
        verify(theMovieDBRepository).getAllMovies()
        assertNotNull(movieEntities)
        assertEquals(5, movieEntities?.size)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovies)
    }
}