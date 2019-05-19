package com.example.movieapi

import io.reactivex.Observable
import retrofit2.http.GET

interface MovieApiService {
    @GET("json/movies.json")
    fun getMovies(): Observable<List<MovieData>>

}