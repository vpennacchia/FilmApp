package com.example.filmapp.navigation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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


@SuppressLint("StateFlowValueCalledInComposition")
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
            val myListMovies by filmViewModel.myListMovies.collectAsState()
            val favMovies by filmViewModel.favoritesMovies.collectAsState()
            if (movie != null) {
                MovieDetailScreen(movie = movie, {
                    if (!myListMovies.contains(movie)) {
                        filmViewModel.addMyListMovie(movie)
                    } else {
                        filmViewModel.removeMyListMovie(movie.id)
                    }
                },
                {
                    if (!favMovies.contains(movie)) {
                        Log.d("prova", movie.id.toString())
                        filmViewModel.addFavoriteMovie(movie)
                    } else {
                        filmViewModel.removeFavoriteMovie(movie.id)
                    }
                }
                , filmViewModel)
            }
        }

        composable(route = Screen.MovieScreen.MyAccount.dRoute) {
            val myListMovies by filmViewModel.myListMovies.collectAsState()
            val favMovies by filmViewModel.favoritesMovies.collectAsState()
            AccountScreen(navigateToDetail = {
                navController.currentBackStackEntry?.savedStateHandle?.set("mov", it)
                navController.navigate(Screen.MovieScreen.Details.dRoute)
            },  myList = myListMovies, favorites = favMovies, authViewModel = authViewModel)
        }

    }
}