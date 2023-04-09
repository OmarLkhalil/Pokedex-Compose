package com.composable.poekedex.pokemonlist

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import com.composable.poekedex.R
import com.composable.poekedex.data.models.PokedexListEntry
import com.composable.poekedex.ui.theme.RobotoCondensed
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter

/**
 * Composable function that represents the PokemonListScreen.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "PokemonLogo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                hint = "Search Pokemon"
            ){}
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}

/**
 * Composable function that represents a search bar with a hint.
 *
 * @param modifier The modifier to be applied to the search bar.
 * @param hint The hint to be displayed in the search bar.
 * @param onSearch The function to be executed when the user searches.
 */
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    // State to hold the current text in the search bar
    var text by remember {
        mutableStateOf("")
    }

    // State to determine if the hint should be displayed
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}
/**
 * Composable function that displays the list of pokemons using a lazy column.
 *
 * @param navController The [NavController] that will be used for navigation between screens.
 * @param viewModel The [PokemonListViewModel] that will be used for getting the list of pokemons.
 */

@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
){
    // Retrieve the required states from the view model
    val pokemonList by remember {viewModel.pokemonList}
    val endReached by remember {viewModel.endReached}
    val loadError by remember {viewModel.loadError}
    val isLoading by remember {viewModel.isLoading}

    // Display the list of pokemons using a lazy column
    LazyColumn(contentPadding = PaddingValues(16.dp)){
        val itemCount = if(pokemonList.size % 2 ==0){
            pokemonList.size / 2
        }
        else {
            pokemonList.size / 2 + 1
        }
        items(itemCount){
            // Load more pokemons when end of list is reached
            if(it >= itemCount -1 && !endReached){
                viewModel.loadPokemonPaginated()
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }

    // Show a progress indicator or a retry button if an error occurred
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if(isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if(loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }
}


/**
 * A composable function that displays a single entry in the Pokedex.
 *
 * @param entry the [PokedexListEntry] to display
 * @param navController the [NavController] to use for navigation
 * @param modifier optional [Modifier] for styling/layout
 * @param viewModel optional [PokemonListViewModel] used to calculate the dominant color
 */
@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel(),
    builder : ImageRequest.Builder.() -> Unit
) {
    // Initialize the default dominant color
    val defaultDominantColor = MaterialTheme.colors.surface

    // Initialize a mutable state variable to hold the dominant color
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    // Display a box that contains the Pokedex entry
    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(listOf(dominantColor, defaultDominantColor))
            )
            .clickable {
                // Navigate to the Pokemon detail screen with the dominant color and name as arguments
                navController.navigate(
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }
    ) {
        Column {
            // Display an image of the Pokemon using CoilImage
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)
            ) {
                Image(
                    painter = rememberImagePainter(
                        data = entry.imageUrl,
                        builder = builder
                    ),
                    contentDescription = entry.pokemonName,
                    modifier = Modifier.fillMaxSize()
                )
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    elevation = 8.dp,
                    modifier = Modifier.size(120.dp)
                ) {
                    Box(Modifier.background(color = dominantColor)) {}
                }

            }
            // Display the name of the Pokemon
            Text(
                text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * A composable function that displays a row of Pokedex entries.
 *
 * @param rowIndex the index of the row to display
 * @param entries a list of [PokedexListEntry] objects to display in the row
 * @param navController the [NavController] to use for navigation
 */
@Composable
fun PokedexRow(
    rowIndex: Int,
    entries: List<PokedexListEntry>,
    navController: NavController,
    builder: ImageRequest.Builder.() -> Unit = { },

    ) {
    Column {
        // Display a row of two Pokedex entries
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
                builder = builder
            )
            Spacer(modifier = Modifier.width(16.dp))
            if(entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                    builder = builder
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

/**
 * Composable function that displays an error message and a retry button.
 *
 * @param error The error message to display.
 * @param onRetry The callback to be invoked when the retry button is clicked.
 */
@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}