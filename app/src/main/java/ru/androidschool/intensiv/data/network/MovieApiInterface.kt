package ru.androidschool.intensiv.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig
import ru.androidschool.intensiv.data.dto.MovieCredits
import ru.androidschool.intensiv.data.dto.MovieInfo
import ru.androidschool.intensiv.data.dto.MovieResponse

interface MovieApiInterface {

    @GET("movie/upcoming/")
    fun upcomingMoviesRequest(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse?>

    @GET("movie/popular/")
    fun popularMoviesRequest(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse?>

    @GET("movie/now_playing/")
    fun nowPlayingMoviesRequest(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse?>

    @GET("tv/popular/")
    fun tvShowRequest(@Query("api_key") apiKey: String = API_KEY): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun movieInfoByIdRequest(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<MovieInfo?>

    @GET("movie/{movie_id}/credits")
    fun movieCreditsByIdRequest(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<MovieCredits?>

    companion object {
        private const val API_KEY = BuildConfig.THE_MOVIE_DATABASE_API
    }
}
