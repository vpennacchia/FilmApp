package com.example.filmapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.filmapp.AccountScreen
import com.example.filmapp.viewmodel.MainViewModel
import com.example.filmapp.MovieDetailScreen
import com.example.filmapp.MovieScreen
import com.example.filmapp.dataFirebase.Movie
import com.example.filmapp.viewmodel.AuthViewModel
import com.example.filmapp.viewmodel.MovieViewModel
import eu.tutorials.chatroomapp.screen.LoginScreen
import eu.tutorials.chatroomapp.screen.SignUpScreen


@Composable
fun Navigation(navController: NavHostController, authViewModel: AuthViewModel) {

    val filmViewModel: MovieViewModel = viewModel()
    val genreViewModel: MainViewModel = viewModel()

    NavHost(navController = navController, startDestination = Screen.MovieScreen.SignUp.dRoute ) {

        composable(Screen.MovieScreen.SignUp.dRoute) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.MovieScreen.Login.dRoute) }
            )
        }
        composable(Screen.MovieScreen.Login.dRoute) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.MovieScreen.SignUp.dRoute) }
            ) {
                navController.navigate(Screen.MovieScreen.Home.dRoute)
            }
        }

        composable(route = Screen.MovieScreen.Home.dRoute) {
            MovieScreen( navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.MovieScreen.Details.dRoute)
            }, navController, viewModel = genreViewModel)
        }
        composable(route = Screen.MovieScreen.Details.dRoute) {
            val movie = navController.previousBackStackEntry?.savedStateHandle?.get<Movie>("mov")
            var favMovies = filmViewModel.getFavorites()
            if (movie != null) {
                MovieDetailScreen(movie = movie, {
                    if (!favMovies.contains(movie)) {
                        filmViewModel.addFavorite(movie)
                    } else {
                        filmViewModel.removeFavorite(movie.id)
                    }
                })
            }
        }

        composable(route = Screen.MovieScreen.MyAccount.dRoute) {
            AccountScreen(navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.MovieScreen.Details.dRoute)
            },  favoritesList = filmViewModel.getFavorites(), authViewModel = authViewModel)
        }

    }
}