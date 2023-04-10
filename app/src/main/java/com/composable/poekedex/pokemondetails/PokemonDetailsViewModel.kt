package com.composable.poekedex.pokemondetails

import androidx.lifecycle.ViewModel
import com.composable.poekedex.repository.PokeRepository
import com.composable.poekedex.utils.Resource
import com.composable.poekedex.data.remote.resposnes.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the Pokemon Details screen. Uses [PokeRepository] to fetch data.
 */

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(private val repositroy: PokeRepository) : ViewModel()
{
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> = repositroy.getPokemonInfo(pokemonName)
}