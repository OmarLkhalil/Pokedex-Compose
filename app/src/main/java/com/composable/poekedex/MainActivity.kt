package com.composable.poekedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.composable.poekedex.pokemonlist.PokemonListScreen
import com.composable.poekedex.ui.theme.JetpackComposePokedexTheme

/**
 * The main activity of the Pokedex app. Extends ComponentActivity, which is a subclass of
 * Activity that adds support for lifecycle and other architectural components.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content of the activity using Jetpack Compose.
        setContent {
            // Apply a theme to the entire app.
            JetpackComposePokedexTheme {
                // Create a NavController, which will handle navigation between screens.
                val navController = rememberNavController()
                // Create a NavHost, which will contain the app's screens.
                NavHost(navController = navController, startDestination = "pokemon_list_screen"){
                    // Add a composable for the "pokemon_list_screen".
                    composable("pokemon_list_screen"){
                       PokemonListScreen(navController = navController)
                    }
                    // Add a composable for the "pokemon_detail_screen".
                    composable("pokemon_detail_screen", arguments = listOf(
                        // Add two arguments to the "pokemon_detail_screen".
                        navArgument("dominantColor"){
                            type = NavType.IntType
                        },
                        navArgument("pokemonName"){
                            type = NavType.StringType
                        }
                    ))
                    {
                        // Retrieve the values of the arguments from the NavBackStackEntry.
                        val dominantColor = remember {
                            it.arguments?.getInt("dominantColor")
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                        // TODO: Display the details of the selected Pokemon using the retrieved arguments.
                    }
                }
            }
        }
    }
}
