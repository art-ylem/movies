package ru.androidschool.intensiv.domain.usecase

import ru.androidschool.intensiv.applySchedulers
import ru.androidschool.intensiv.domain.repository.FeedRepository
import javax.inject.Inject
import javax.inject.Named

class FeedUseCase (private var repository: FeedRepository) {

    fun getMovies() = repository.getMovies().applySchedulers()
}
