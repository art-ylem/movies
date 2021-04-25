package ru.androidschool.intensiv.domain.usecase

import ru.androidschool.intensiv.applySchedulers
import ru.androidschool.intensiv.domain.repository.TvShowRepository

class TvShowUseCase(private var repository: TvShowRepository) {

    fun getTvShows() = repository.getTvShow().applySchedulers()
}
