package com.composable.poekedex.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.palette.graphics.Palette
import com.composable.poekedex.repository.PokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


/**
 * ViewModel for the Pokemon List screen. Uses [PokeRepository] to fetch data and exposes
 * [calcDomaintColor] function to calculate the dominant color of a given Drawable.
 */
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokeRepository
) : ViewModel() {

    /**
     * Calculates the dominant color of the given [drawable] using the [Palette] API and invokes
     * [onFinish] with the resulting [Color].
     *
     * @param drawable The drawable for which to calculate the dominant color.
     * @param onFinish The callback function to invoke with the resulting [Color].
     */
    fun calcDomaintColor(drawable: Drawable, onFinish: (Color) -> Unit){
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}
