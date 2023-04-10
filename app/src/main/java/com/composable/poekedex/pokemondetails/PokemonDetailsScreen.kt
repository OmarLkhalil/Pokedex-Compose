package com.composable.poekedex.pokemondetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.composable.poekedex.utils.Resource
import com.composable.poekedex.data.remote.resposnes.Pokemon


/**
 * Composable that displays the details of a Pokémon.
 *
 * @param dominantColor the dominant color to use as the background
 * @param pokemonName the name of the Pokémon to display
 * @param navController the NavController to use for navigation
 * @param topPadding the top padding to use for the content
 * @param pokemonImageSize the size of the Pokémon image to display
 * @param viewModel the view model to use for getting the Pokémon information
 */
@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailsViewModel = hiltViewModel()
) {
    // Produce the state of the Pokémon resource
    val pokemonInfo = produceState<Resource<Pokemon>>(
        initialValue = Resource.Loading()
    ) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value

    // Display the components of the Pokémon detail screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        PokemonDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            // If the Pokémon information is successfully loaded, display its image
            if(pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.frontDefault)
                            .build(),
                        contentDescription = pokemonInfo.data.name,
                        loading = {
                            CircularProgressIndicator(
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.scale(0.5f)
                            )
                        },
                        success = {
                            Box(
                                modifier = Modifier
                                    .size(pokemonImageSize)
                                    .offset(y = topPadding)
                            )
                        },
                    )
                }
            }
        }
    }
}

/**
Composable that displays the top section of the Pokemon detail screen, including the back button.
@param navController The NavController used to navigate back.
@param modifier Optional Modifier to apply to the composable.
 */
@Composable
fun PokemonDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

/**

Composable that displays the state of the Pokemon detail screen based on the provided [pokemonInfo].

@param pokemonInfo The [Resource] containing the Pokemon information to display.

@param modifier Optional Modifier to apply to the composable.

@param loadingModifier Optional Modifier to apply to the loading indicator.
 */
@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokemonInfo) {
        is Resource.Success -> {

        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}