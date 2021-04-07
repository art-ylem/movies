package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.MovieResponse

interface MovieApiInterface {
    @GET("3/movie/upcoming/")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Call<MovieResponse>
}

// https://api.themoviedb.org/3/movie/upcoming/api_key?api_key=6c0c3ea49bc5ef04693f43ca57c98619
// https://api.themoviedb.org/3/movie/upcoming?api_key=6c0c3ea49bc5ef04693f43ca57c98619&language=en-US&page=1