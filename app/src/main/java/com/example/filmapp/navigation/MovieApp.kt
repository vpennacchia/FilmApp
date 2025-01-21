package com.example.filmapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmapp.FavoritesScreen
import com.example.filmapp.MainViewModel
import com.example.filmapp.MovieDetailScreen
import com.example.filmapp.MovieScreen
import com.example.filmapp.objects.Movie


@Composable
fun MovieApp(navController: NavHostController) {

    val filmViewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.MovieScreen.Home.dRoute ) {
        composable(route = Screen.MovieScreen.Home.dRoute) {
            MovieScreen( navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.MovieScreen.Details.dRoute)
            }, navController, viewModel = filmViewModel)
        }
        composable(route = Screen.MovieScreen.Details.dRoute) {
            val category = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("mov")
            if (category != null) {
                MovieDetailScreen(movie = category, {
                    if (filmViewModel.favorites.contains(category)) {
                        filmViewModel.favorites.remove(category)
                    } else {
                        filmViewModel.favorites.add(category)
                    }
                })
            }
        }

        composable(route = Screen.MovieScreen.Favorites.dRoute) {
            FavoritesScreen(navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.MovieScreen.Details.dRoute)
            },  favoritesList = filmViewModel.favorites)
        }

    }
}