package ru.androidschool.intensiv.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.androidschool.intensiv.data.BaseViewModel
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.repository.FeedRemoteRepository
import ru.androidschool.intensiv.domain.usecase.FeedUseCase
import timber.log.Timber
import javax.inject.Inject

class FeedViewModel : BaseViewModel<FeedViewModel.ViewState>() {
    @Inject lateinit var feedUseCase: FeedUseCase

    private var _uiStateLiveData: MutableLiveData<List<ViewState>> = MutableLiveData()
    fun uiStateLiveData(): LiveData<List<ViewState>> = _uiStateLiveData


    fun load() {

        val dis = feedUseCase.getMovies().doOnSubscribe { ViewState.ShowLoadingViewState }
            .doFinally { ViewState.HideLoadingViewState }
            .subscribe({ ViewState.OnSuccessState(it) }, { err ->
                ViewState.ErrorState
                Timber.e(err)
            })
        cd.add(dis)
    }

    init {
//        DaggerAppComponent.builder().build().inject(this)
        load()
    }

    sealed class ViewState {
        object ShowLoadingViewState : ViewState()
        object HideLoadingViewState : ViewState()
        object ErrorState : ViewState()
        class OnSuccessState(val onSuccessData: Any?) :
            ViewState()
    }
}