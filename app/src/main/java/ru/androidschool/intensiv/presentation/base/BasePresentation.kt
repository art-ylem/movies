package ru.androidschool.intensiv.presentation.base

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {

    internal val cd by lazy { CompositeDisposable() }
    internal var view: V? = null

    fun attachView(view: V) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    fun disposeObservables() = cd.clear()
}
