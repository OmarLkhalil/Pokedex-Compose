package com.composable.poekedex.repository

import com.composable.poekedex.data.remote.PokeApi
import com.composable.poekedex.utils.Resource
import com.composable.poekedex.data.remote.resposnes.Pokemon
import com.composable.poekedex.data.remote.resposnes.PokemonList
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * A repository class that is responsible for making network requests to retrieve data related to Pokemon.
 * @param api An instance of the PokeApi interface, which is used to make network requests.
 */
@ActivityScoped
class PokeRepository @Inject constructor(private val api: PokeApi) {

    /**
     * Retrieves a list of Pokemon from the network API.
     * @param limit The maximum number of Pokemon to retrieve.
     * @param offset The offset used to paginate through the list of Pokemon.
     * @return A Resource object that encapsulates the success or failure of the network request.
     */
    suspend fun getPokemon(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }

    /**
     * Retrieves information about a specific Pokemon from the network API.
     * @param pokemonName The name of the Pokemon to retrieve information about.
     * @return A Resource object that encapsulates the success or failure of the network request.
     */
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon>{
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception){
            return Resource.Error("An unknown error occurred.")
        }
        return Resource.Success(response)
    }
}
