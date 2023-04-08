package com.composable.poekedex.di

import com.composable.poekedex.data.remote.PokeApi
import com.composable.poekedex.utils.Constants.BASE_URL
import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A Dagger module that provides dependencies related to network operations.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of the PokeApi interface using Retrofit.
     * The @Singleton annotation indicates that only one instance of the returned type will be created.
     * @return A singleton instance of the PokeApi interface.
     */
    @Singleton
    @Provides
    fun providePokeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
}
