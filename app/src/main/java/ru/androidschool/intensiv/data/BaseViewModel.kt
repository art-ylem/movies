package ru.androidschool.intensiv.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel<T>() : ViewModel() {

    internal val cd = CompositeDisposable()

    fun MutableLiveData<List<T>>.postUiState(vararg states: T) {
        postValue(states.toList())
    }

    internal fun clearDisposables() = cd.clear()
}
