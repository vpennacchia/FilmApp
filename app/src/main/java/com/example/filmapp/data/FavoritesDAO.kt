package com.example.filmapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoritesDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE) //se cerchiamo di aggiungere un wish già esistente ignoriamo
    abstract suspend fun addFavorite(favoriteEntity: Movie)

    @Query("Select * from `favorites-table`")
    abstract fun getAlFavorites(): Flow<List<Movie>>

    @Delete
    abstract suspend fun deleteFavorite(wishEntity: Movie)

    @Query("SELECT EXISTS(SELECT 1 FROM 'favorites-table' WHERE id = :movieId)")
    abstract suspend fun isMovieInFavorites(movieId: Int): Boolean

}