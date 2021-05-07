package ru.androidschool.intensiv.presentation.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.androidschool.intensiv.data.BaseViewModel
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.repository.FeedRemoteRepository
import ru.androidschool.intensiv.domain.usecase.FeedUseCase
import timber.log.Timber
import javax.inject.Inject

class FeedViewModel @Inject constructor(private var feedUseCase: FeedUseCase) : BaseViewModel<FeedViewModel.ViewState>() {

    private var _uiStateLiveData: MutableLiveData<List<ViewState>> = MutableLiveData()
    fun uiStateLiveData(): LiveData<List<ViewState>> = _uiStateLiveData


    private fun load() {

        val dis = feedUseCase.getMovies().doOnSubscribe { _uiStateLiveData.postUiState(ViewState.ShowLoadingViewState) }
            .subscribe({ _uiStateLiveData.postUiState(ViewState.OnSuccessState(it)) }, { err ->
                _uiStateLiveData.postUiState(ViewState.ErrorState)
                Timber.e(err)
            })
        cd.add(dis)
    }

    init {
        load()
    }

    sealed class ViewState {
        object ShowLoadingViewState : ViewState()
        object HideLoadingViewState : ViewState()
        object ErrorState : ViewState()
        class OnSuccessState(val onSuccessData: HashMap<FeedFragment.BlockMovies, MovieResponse>) :
            ViewState()
    }
}