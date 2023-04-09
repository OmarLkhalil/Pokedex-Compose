package com.composable.poekedex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.composable.poekedex.data.models.PokedexListEntry
import com.composable.poekedex.repository.PokeRepository
import com.composable.poekedex.utils.Constants.PAGE_SIZE
import com.composable.poekedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


/**
 * ViewModel for the Pokemon List screen. Uses [PokeRepository] to fetch data and exposes
 * [calcDomaintColor] function to calculate the dominant color of a given Drawable.
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokeRepository
) : ViewModel() {

    private var curPage = 0

    // A list of Pokemon entries loaded from the API, with each page containing a sublist of entries
    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())

    // A string representing any error that occurred while loading the Pokemon entries
    var loadError   = mutableStateOf("")

    // A boolean representing whether or not the app is currently loading Pokemon entries
    var isLoading   = mutableStateOf(false)

    // A boolean representing whether or not all available Pokemon entries have been loaded
    var endReached   = mutableStateOf(false)


    init {
        loadPokemonPaginated()
    }
    // A function for loading Pokemon entries in a paginated manner
    fun loadPokemonPaginated() {

        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {

            // Indicate that the app is currently loading Pokemon entries
            isLoading.value = true

            // Call the API to get the next page of Pokemon entries
            val result = repository.getPokemon(PAGE_SIZE, curPage * PAGE_SIZE)

            // Handle the result of the API call
            when(result){

                // If the API call was successful
                is Resource.Success ->{

                    // Check if all available Pokemon entries have been loaded
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count

                    // Map the results to a list of PokedexListEntry objects
                    val pokedexEntries = result.data.results.mapIndexed{
                            index, entry ->

                        // Extract the Pokemon number from the URL and construct the image URL
                        val number = if(entry.url.endsWith("/")){
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"

                        // Construct a PokedexListEntry object
                        PokedexListEntry(entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, url, number.toInt())
                    }

                    // Increment the current page number
                    curPage ++

                    // Clear any previous load errors and loading indicator
                    loadError.value = ""
                    isLoading.value = false

                    // Add the new page of Pokemon entries to the pokemonList
                    pokemonList.value += pokedexEntries
                }

                // If the API call failed
                is Resource.Error ->{

                    // Set the load error string and clear the loading indicator
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    /**
     * Calculates the dominant color of the given [drawable] using the [Palette] API and returns
     * the resulting [Color].
     *
     * @param drawable The drawable for which to calculate the dominant color.
     * @return The dominant color of the given [drawable].
     */
    @Composable
    fun calculateDominantColor(drawable: Drawable): Color {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val palette = Palette.from(bitmap).generate()
        val dominantColor = palette?.dominantSwatch?.rgb ?: MaterialTheme.colors.surface.toArgb()
        return Color(dominantColor)
    }

}
