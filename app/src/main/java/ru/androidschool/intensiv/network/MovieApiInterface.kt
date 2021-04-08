package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.data.MovieCredits
import ru.androidschool.intensiv.data.MovieInfo
import ru.androidschool.intensiv.data.MovieResponse

interface MovieApiInterface {

    @GET("3/movie/upcoming/")
    fun getUpcomingMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("3/movie/popular/")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("3/tv/popular/")
    fun getTvShow(@Query("api_key") apiKey: String): Call<MovieResponse>

    @GET("3/movie/{movie_id}")
    fun getMovieInfoById(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String): Call<MovieInfo>

    @GET("3/movie/{movie_id}/credits")
    fun getMovieCreditsById(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String): Call<MovieCredits>

    // не нашел на дизайне где отображать now_playing фильмы
}
