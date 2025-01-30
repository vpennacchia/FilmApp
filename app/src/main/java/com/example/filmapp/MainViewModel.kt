package com.example.filmapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.api.filmService
import com.example.filmapp.data.FavoritesRepository
import com.example.filmapp.data.Genre
import com.example.filmapp.data.GenreResponse
import com.example.filmapp.data.Graph
import com.example.filmapp.data.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    private val _genreState= mutableStateOf(GenreState())
    val genreState: State<GenreState> = _genreState
    var movieByCategories = mutableMapOf<Int, List<Movie>>()
    var favorites = mutableListOf<Movie>()
    private val favoritesRepository: FavoritesRepository = Graph.favRepo

    init {
        fetchGenres()
    }

    fun fetchGenres(){
        viewModelScope.launch {
            try {
                val response = filmService.getGenres()
                organizeByCategory(response)
                _genreState.value = _genreState.value.copy(
                    list = response.genres,
                    loading = false,
                    error = null
                )

            }catch (e: Exception){
                _genreState.value = _genreState.value.copy(
                    loading = false,
                    error = "Error fetching Genres ${e.message}"
                )
            }
        }
    }

    suspend fun organizeByCategory(list: GenreResponse) {
        list.genres.forEach { el ->
            val allMovies = mutableListOf<Movie>()
            var currentPage = 1

            do {
                val response = filmService.getMoviesByGenre("447f42fa4bc5d5ebd07b387dee8385d7", genreId = el.id, page = currentPage)
                allMovies.addAll(response.results)
                currentPage++
            } while (currentPage <= 10)


            movieByCategories[el.id] = allMovies

        }
    }

    fun addFavMovie(movie:Movie) {
        viewModelScope.launch(Dispatchers.IO) { //ottimizza nelle kotlin coroutine le operazioni di IO
            favoritesRepository.addFavorite(favorite = movie)
        }
    }


    fun deleteFavMovie(movie: Movie) {
        viewModelScope.launch(Dispatchers.IO) { //ottimizza nelle kotlin coroutine le operazioni di IO
            favoritesRepository.deleteFavorite(favorite = movie)
        }
    }

    fun getFavMovies(): List<Movie> {
        viewModelScope.launch {
            favorites = favoritesRepository.getFavorites().first().toMutableList()
        }

        return favorites
    }

    data class GenreState(
        val loading: Boolean = true,
        val list: List<Genre> = emptyList(),
        val error: String? = null
    )
}