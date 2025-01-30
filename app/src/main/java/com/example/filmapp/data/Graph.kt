package com.example.filmapp.data

import android.content.Context
import androidx.room.Room

object Graph {
    lateinit var dataBase: FavoritesDataBase

    val favRepo by lazy { //permette di inizializzare una sola volta il db
        FavoritesRepository(favDAO = dataBase.favDao())
    }

    fun provide(context: Context) {
        dataBase = Room.databaseBuilder(context, FavoritesDataBase::class.java, "wishlist.db").build()
    }
}