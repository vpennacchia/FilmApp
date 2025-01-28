package com.example.filmapp.data


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomTypeConverters::class)
abstract class FavoritesDataBase: RoomDatabase() {
    abstract fun favDao():FavoritesDAO
}