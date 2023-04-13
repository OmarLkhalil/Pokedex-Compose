package com.composable.poekedex.pokemonlist


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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.*
import coil.request.ImageRequest
import com.composable.poekedex.R
import com.composable.poekedex.data.models.PokedexListEntry
import com.composable.poekedex.ui.theme.RobotoCondensed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import me.vponomarenko.compose.shimmer.shimmer

/**
 * Composable function that represents the PokemonListScreen.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun PokemonListScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberSwipeRefreshState(isRefreshing = false)
        SwipeRefresh(state = state,
            onRefresh = { viewModel.RefreshPokemonList()
            state.isRefreshing = false
            }
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
                ) {
                    viewModel.searchPokemonList(it)
                }
                Spacer(modifier = Modifier.height(10.dp))
                PokemonList(navController = navController)
            }
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
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
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
) {
    // Retrieve the required states from the view model
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }


    // Display the list of pokemons using a lazy column
    LazyColumn(contentPadding = PaddingValues(15.dp)) {
        // calculate the number of items to be displayed based on the size of the pokemonlist
        // The formula used here no ensure that each row contains two items except for the last row if the list size is odd,
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }
        items(itemCount) {
            // Load more pokemons when end of list is reached
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadPokemonPaginated()
                }
            }
            PokedexRow(rowIndex = it, entries = pokemonList, navController = navController)
        }
    }

    // Show a progress indicator or a retry button if an error occurred
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }
        if (loadError.isNotEmpty()) {
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
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }
    Box(
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultDominantColor
                    )
                )
            )
            .clickable {
                navController.navigate(
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }
    ) {
        Column(
            horizontalAlignment = CenterHorizontally,
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .build(),
                contentDescription = entry.pokemonName,
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .height(120.dp)
                            .shimmer(
                                durationMs = 1000,
                            )
                            .background(
                                color = Color(0xFFF3F3F3),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                },
                success = { success ->
                    dominantColor = viewModel.calculateDominantColor(success.result.drawable)
                    Image(
                        bitmap = success.result.drawable.toBitmap().asImageBitmap(),
                        contentDescription = entry.pokemonName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .padding()
                    )
                }
            )
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
) {
    Column {
        // Display a row of two Pokedex entries
        Row {
            PokedexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokedexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
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