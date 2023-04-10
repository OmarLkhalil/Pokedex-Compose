package com.composable.poekedex.data.remote

import com.composable.poekedex.data.remote.resposnes.Pokemon
import com.composable.poekedex.data.remote.resposnes.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ) : Pokemon
}