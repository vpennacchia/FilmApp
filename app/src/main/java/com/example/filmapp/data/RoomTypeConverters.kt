package com.example.filmapp.data
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object RoomTypeConverters {
    @TypeConverter
    fun fromList(list: List<Int?>?): String? {
        if (list == null) return null
        return Gson().toJson(list) // Converte la lista in formato JSON
    }

    @TypeConverter
    fun fromString(value: String?): List<Int>? {
        if (value == null) return null
        val listType = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType) // Converte la stringa JSON in una lista
    }
}