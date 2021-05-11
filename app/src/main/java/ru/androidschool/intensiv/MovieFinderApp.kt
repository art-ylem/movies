package ru.androidschool.intensiv

import android.app.Application
import ru.androidschool.intensiv.di.AppComponent
import ru.androidschool.intensiv.di.DaggerAppComponent
import timber.log.Timber

class MovieFinderApp : Application() {

    val component: AppComponent =
        DaggerAppComponent
            .builder().build()



    override fun onCreate() {
        super.onCreate()
        instance = this
        initDebugTools()
        component.inject(this)
    }

    private fun initDebugTools() {
        if (BuildConfig.DEBUG) {
            initTimber()
        }
    }

    private fun initTimber() {
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        var instance: MovieFinderApp? = null
            private set
    }
}
