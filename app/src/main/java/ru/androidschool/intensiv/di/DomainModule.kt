package ru.androidschool.intensiv.di

import dagger.Module
import dagger.Provides
import ru.androidschool.intensiv.data.network.MovieApiInterface
import ru.androidschool.intensiv.data.repository.FeedRemoteRepository
import ru.androidschool.intensiv.domain.repository.FeedRepository
import ru.androidschool.intensiv.domain.usecase.FeedUseCase
import javax.inject.Named
import javax.inject.Singleton

@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideRemoteFeedUseCase(feedRepository: FeedRemoteRepository) = FeedUseCase(feedRepository)

    @Singleton
    @Provides
    @Named("remote")
    fun provideFeedRepository(api: MovieApiInterface) = FeedRemoteRepository(api)


}