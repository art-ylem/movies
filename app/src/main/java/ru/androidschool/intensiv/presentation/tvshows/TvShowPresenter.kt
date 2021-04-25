package ru.androidschool.intensiv.presentation.tvshows

import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.domain.usecase.TvShowUseCase
import ru.androidschool.intensiv.presentation.base.BasePresenter
import timber.log.Timber

class TvShowPresenter(val useCase: TvShowUseCase) : BasePresenter<TvShowPresenter.TvShowView>() {

    fun getData() {
        val dis = useCase.getTvShows()
            .doOnSubscribe { view?.showLoading() }
            .doFinally { view?.hideLoading() }
            .subscribe(
                { respond ->
                    respond.results?.let { movies -> view?.showMovies(movies) }
                },
                {
                    view?.showError()
                    Timber.e(it)
                }
            )

        cd.add(dis)
    }

    interface TvShowView {
        fun showMovies(movies: List<Movie>)
        fun showLoading()
        fun hideLoading()
        fun showError()
    }
}
