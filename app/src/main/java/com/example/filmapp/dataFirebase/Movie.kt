package com.example.filmapp.dataFirebase
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int = 0,
    val adult: Boolean = false,
    val backdrop_path: String? = "",
    val genre_ids: List<Int> = emptyList(),
    val original_language: String = "",
    val original_title: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val poster_path: String = "",
    val release_date: String = "",
    val title: String = "",
    val video: Boolean = false,
    val vote_average: Double = 0.0,
    val vote_count: Int = 0
): Parcelable

data class MovieResponse(
    val results: List<Movie>
)