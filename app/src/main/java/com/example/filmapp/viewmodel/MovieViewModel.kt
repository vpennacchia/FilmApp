package com.example.filmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.dataFirebase.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    var favoriteMovies = mutableListOf<Movie>()

    fun getFavorites(): MutableList<Movie> {
        viewModelScope.launch {
            MovieRepository.getFavorites().collect {
                _favoriteMovies.value = it
            }

            favoriteMovies = _favoriteMovies.first().toMutableList()
        }

        return favoriteMovies
    }

    fun addFavorite(movie: Movie) {
        viewModelScope.launch {
            MovieRepository.addFavorite(movie)
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            MovieRepository.removeFavorite(movieId)
        }
    }
}
