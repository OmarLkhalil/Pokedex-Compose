package com.composable.poekedex.di

import com.composable.poekedex.data.remote.PokeApi
import com.composable.poekedex.repository.PokeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokeRepository(
        api: PokeApi
    ) = PokeRepository(api)
}