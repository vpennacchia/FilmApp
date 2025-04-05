package com.example.filmapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.filmapp.navigation.Navigation
import com.example.filmapp.ui.theme.FilmAppTheme
import com.example.filmapp.viewmodel.AuthViewModel
import com.example.filmapp.viewmodel.MainViewModel
import com.example.filmapp.viewmodel.MovieViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()
            val filmViewModel: MovieViewModel = viewModel()
            val genreViewModel: MainViewModel = viewModel()

            FilmAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(navController, authViewModel, filmViewModel, genreViewModel)
                }
            }
        }
    }
}
