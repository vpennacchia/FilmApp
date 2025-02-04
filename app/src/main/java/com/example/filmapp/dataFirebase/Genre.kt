package com.example.filmapp.dataFirebase

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Int,
    val name: String,
) : Parcelable

data class GenreResponse(val genres: List<Genre>)