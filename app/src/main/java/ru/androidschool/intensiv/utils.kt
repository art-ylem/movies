package ru.androidschool.intensiv

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.androidschool.intensiv.network.MovieApiClient

//QUESTION: такие extensions? подавлять checkResult?

@SuppressLint("CheckResult")
fun <T> Observable<T>.myObserve(): Observable<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

@SuppressLint("CheckResult")
fun <T> Single<T>.myObserve(): Single<T> =
    this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

fun Double?.toRating(): Float = (this?.div(2))?.toFloat() ?: 0F

val retrofit = MovieApiClient.apiClient
