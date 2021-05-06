package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.network.MovieApiInterface
import ru.androidschool.intensiv.domain.repository.FeedRepository
import ru.androidschool.intensiv.presentation.feed.FeedFragment
import javax.inject.Inject

class FeedRemoteRepository (private val retrofit: MovieApiInterface) : FeedRepository {

    override fun getMovies(): Single<HashMap<FeedFragment.BlockMovies, MovieResponse>> {
        return Single.zip(
            retrofit.popularMoviesRequest().onErrorResumeNext { Single.just(null) },
            retrofit.upcomingMoviesRequest().onErrorResumeNext { Single.just(null) },
            retrofit.nowPlayingMoviesRequest().onErrorResumeNext { Single.just(null) },
            { popular, upcoming, nowPlaying ->
                hashMapOf(
                    FeedFragment.BlockMovies.UPCOMING to popular,
                    FeedFragment.BlockMovies.POPULAR to upcoming,
                    FeedFragment.BlockMovies.NOW_PLAYING to nowPlaying
                )
            })
    }
}
