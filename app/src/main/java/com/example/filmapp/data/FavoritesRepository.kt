package com.example.filmapp.data

import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val favDAO: FavoritesDAO) {

    suspend fun addFavorite(favorite:Movie) {
        favDAO.addFavorite(favorite)
    }

    fun getFavorites(): Flow<List<Movie>> = favDAO.getAlFavorites()


    suspend fun deleteFavorite(favorite: Movie) {
        favDAO.deleteFavorite(favorite)
    }

    suspend fun isFavorite(movieId: Int): Boolean {
        return favDAO.isMovieInFavorites(movieId)
    }

}