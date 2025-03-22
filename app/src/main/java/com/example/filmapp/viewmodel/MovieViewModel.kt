package com.example.filmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.dataFirebase.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _favoriteMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoriteMovies: StateFlow<List<Movie>> = _favoriteMovies

    init {
        getFavorites()
    }

    private fun getFavorites() {
        viewModelScope.launch {
            MovieRepository.getFavorites().collect {
                _favoriteMovies.value = it // Aggiorna lo stato
            }
        }
    }

    fun addFavorite(movie: Movie) {
        viewModelScope.launch {
            MovieRepository.addFavorite(movie)
            _favoriteMovies.value += movie
        }
    }

    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            MovieRepository.removeFavorite(movieId)
            _favoriteMovies.value = _favoriteMovies.value.filter { it.id != movieId }
        }
    }
}
