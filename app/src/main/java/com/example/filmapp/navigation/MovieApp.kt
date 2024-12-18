package com.example.filmapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmapp.MovieDetailScreen
import com.example.filmapp.MovieScreen
import com.example.filmapp.objects.Movie


@Composable
fun MovieApp(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.MovieScreen.route ) {
        composable(route = Screen.MovieScreen.route) {
            MovieScreen( navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.DetailScreen.route)
            })
        }
        composable(route = Screen.DetailScreen.route) {
            val category = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("mov")
            if (category != null) {
                MovieDetailScreen(movie = category)
            }
        }

    }
}