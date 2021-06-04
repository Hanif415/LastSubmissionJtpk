package com.koshka.submissionl.service

import com.koshka.submissionl.data.remote.response.MovieResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.themoviedb.org"
private const val API_KEY = "1a039490d63fa4a2923663cb26e46c35"

val loggingHttpInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

val client = OkHttpClient.Builder().addInterceptor(loggingHttpInterceptor).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface MovieApiService {
    @GET("https://api.themoviedb.org/3/movie/popular?api_key=$API_KEY&language=en-US&page=1")
    fun getMovies(): List<MovieResponse>
}

object MovieApi {
    val movieApiService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}