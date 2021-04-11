package ru.androidschool.intensiv.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.MovieCredits
import ru.androidschool.intensiv.data.MovieInfo
import ru.androidschool.intensiv.data.MovieResponse

interface MovieApiInterface {

    @GET("movie/upcoming/")
    fun getUpcomingMovies(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse>

    @GET("movie/popular/")
    fun getPopularMovies(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse>

    @GET("tv/popular/")
    fun getTvShow(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieInfoById(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String = API_KEY): Single<MovieInfo>

    @GET("movie/{movie_id}/credits")
    fun getMovieCreditsById(@Path("movie_id") movieId: String, @Query("api_key") apiKey: String = API_KEY): Single<MovieCredits>

    companion object {
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}
