package com.example.filmapp.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmapp.api.filmService
import com.example.filmapp.dataFirebase.Genre
import com.example.filmapp.dataFirebase.GenreResponse
import com.example.filmapp.dataFirebase.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel: ViewModel() {

    private val _genreState= mutableStateOf(GenreState())
    val genreState: State<GenreState> = _genreState
    var movieByCategories = mutableMapOf<Int, List<Movie>>()
    private val _providers = MutableStateFlow<List<String>>(emptyList())
    val providers: StateFlow<List<String>> = _providers

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

    fun getMovieProviders(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = filmService.getMovieWatchProviders(movieId, "447f42fa4bc5d5ebd07b387dee8385d7")
                val italianProviders = response.results?.get("IT")?.flatrate?.map { it.providerName }  ?: response.results?.get("IT")?.rent?.map { it.providerName } ?: response.results?.get("IT")?.buy?.map { it.providerName } ?: emptyList()
                _providers.value = italianProviders
            } catch (e: Exception) {
                e.printStackTrace()
                _providers.value = emptyList()
            }
        }
    }

    fun getProviderLink(providerName: String): String {
        val providerUrl = when (providerName) {
            "netflix" -> "https://www.netflix.com/"
            "amazon video" -> "https://www.primevideo.com/"
            "disney plus" -> "https://www.disneyplus.com/"
            "rakuten" -> "https://www.rakuten.tv/it"
            "apple tv" -> "https://tv.apple.com/it"
            "google" -> "https://tv.google/intl/it_it/"
            "microsoft store" -> "https://www.microsoft.com/it-it/store/movies-and-tv"
            "sky go" -> "https://skygo.sky.it/"
            else -> ""
        }

        return providerUrl
    }

    data class GenreState(
        val loading: Boolean = true,
        val list: List<Genre> = emptyList(),
        val error: String? = null
    )
}