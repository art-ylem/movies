package ru.androidschool.intensiv.di

import dagger.Component
import dagger.Module
import ru.androidschool.intensiv.MovieFinderApp
import ru.androidschool.intensiv.domain.repository.FeedRepository
import ru.androidschool.intensiv.domain.usecase.FeedUseCase
import ru.androidschool.intensiv.presentation.feed.FeedFragment
import ru.androidschool.intensiv.presentation.feed.FeedViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, DomainModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(feedFragment: FeedFragment)
    fun inject(app: MovieFinderApp)
    fun inject(feedUseCase: FeedUseCase)
    fun inject(feedViewModel: FeedViewModel)
}