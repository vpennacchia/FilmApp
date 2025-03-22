package com.example.filmapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.dataFirebase.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel : ViewModel() {
    private val _myListMovies = MutableStateFlow<List<Movie>>(emptyList())
    val myListMovies: StateFlow<List<Movie>> = _myListMovies

    private val _favoritesMovies = MutableStateFlow<List<Movie>>(emptyList())
    val favoritesMovies: StateFlow<List<Movie>> = _favoritesMovies

    init {
        getMyList()
        getFavorites()
    }

    private fun getMyList() {
        viewModelScope.launch {
            MovieRepository.getMyListMovies().collect {
                _myListMovies.value = it // Aggiorna lo stato
            }
        }
    }

    fun addMyListMovie(movie: Movie) {
        viewModelScope.launch {
            MovieRepository.addMovieInMyList(movie)
            _myListMovies.value += movie
        }
    }

    fun removeMyListMovie(movieId: Int) {
        viewModelScope.launch {
            MovieRepository.removeMovieInMyList(movieId)
            _myListMovies.value = _myListMovies.value.filter { it.id != movieId }
        }
    }

    private fun getFavorites() {
        viewModelScope.launch {
            MovieRepository.getFavoritesMovies().collect {
                _favoritesMovies.value = it // Aggiorna lo stato
            }
        }
    }

    fun addFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            MovieRepository.addFavoritesMovie(movie)
            _favoritesMovies.value += movie
        }
    }

    fun removeFavoriteMovie(movieId: Int) {
        viewModelScope.launch {
            MovieRepository.removeFavoritesMovie(movieId)
            _favoritesMovies.value = _favoritesMovies.value.filter { it.id != movieId }
        }
    }
}
