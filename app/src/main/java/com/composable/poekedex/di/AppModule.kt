package com.composable.poekedex.di

import com.composable.poekedex.data.remote.PokeApi
import com.composable.poekedex.repository.PokeRepository
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * A Dagger module that provides dependencies for the entire application.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provides a singleton instance of the PokeRepository class.
     * The @Singleton annotation indicates that only one instance of the returned type will be created.
     * @param api An instance of the PokeApi interface, which is used to make network requests.
     * @return A singleton instance of the PokeRepository class.
     */
    @Singleton
    @Provides
    fun providePokeRepository(
        api: PokeApi
    ) = PokeRepository(api)
}
