package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.domain.repository.TvShowRepository
import ru.androidschool.intensiv.retrofit

class TvShowRemoteRepository : TvShowRepository {
    override fun getTvShow(): Single<MovieResponse> {
        return retrofit.tvShowRequest()
    }
}
