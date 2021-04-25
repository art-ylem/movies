package ru.androidschool.intensiv.domain.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.MovieResponse

interface TvShowRepository {
    fun getTvShow(): Single<MovieResponse>
}
