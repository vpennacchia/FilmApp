package com.example.filmapp.api
import com.example.filmapp.dataFirebase.GenreResponse
import com.example.filmapp.dataFirebase.MovieResponse
import com.example.filmapp.dataFirebase.WatchProvidersResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
    .addConverterFactory(GsonConverterFactory.create()).build()


val filmService = retrofit.create(MovieService::class.java)


interface MovieService {

    @GET("discover/movie")
    suspend fun getMoviesByGenre(
        @Query("api_key") apiKey: String = "447f42fa4bc5d5ebd07b387dee8385d7",
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): MovieResponse

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = "447f42fa4bc5d5ebd07b387dee8385d7"
    ): GenreResponse

    @GET("movie/{movie_id}/watch/providers")
    suspend fun getMovieWatchProviders(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "it-IT"
    ): WatchProvidersResponse
}