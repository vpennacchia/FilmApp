package com.example.filmapp.dataFirebase
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
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

data class WatchProvidersResponse(
    @SerializedName("results") val results: Map<String, ProviderInfo>?
)

data class ProviderInfo(
    @SerializedName("flatrate") val flatrate: List<ProviderDetail>?,
    @SerializedName("buy") val buy: List<ProviderDetail>?,
    @SerializedName("rent") val rent: List<ProviderDetail>?
)

data class ProviderDetail(
    @SerializedName("provider_name") val providerName: String,
    @SerializedName("logo_path") val logoPath: String
)

data class MovieTrailerDetail(
    @SerializedName("id") val trailerId: Int,
    @SerializedName("results") val details: List<TrailerDetail>
)

data class TrailerDetail(
    @SerializedName("iso_639_1") val providerName: String,
    @SerializedName("iso_3166_1") val logoPath: String,
    @SerializedName("name") val name: String,
    @SerializedName("key") val key: String,
    @SerializedName("site") val site: String,
    @SerializedName("size") val size: Int,
    @SerializedName("type") val type: String,
    @SerializedName("official") val official: Boolean,
    @SerializedName("published_at") val published_at: String,
    @SerializedName("id") val id: String
)

data class MovieResponse(
    val results: List<Movie>
)