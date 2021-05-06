package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.presentation.feed.FeedFragment
import javax.inject.Inject

interface FeedRepository {
    fun getMovies(): Single<HashMap<FeedFragment.BlockMovies, MovieResponse>>
}
