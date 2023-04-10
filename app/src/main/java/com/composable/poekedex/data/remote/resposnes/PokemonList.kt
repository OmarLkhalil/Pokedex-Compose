package com.composable.poekedex.data.remote.resposnes


data class PokemonList(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)