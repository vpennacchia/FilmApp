package com.example.filmapp.test

import com.example.filmapp.api.filmService
import com.example.filmapp.objects.GenreResponse
import com.example.filmapp.objects.Movie
import com.example.filmapp.objects.MovieResponse

class test {

    suspend fun testGetMoviesByGenre() {
        //val list = mutableListOf(28, 12, 16, 35, 80, 99)
        //var categoryMovies = mutableMapOf<Int, MovieResponse>()
        //list.forEach { el ->
        //    var i = 0
        //    while(i <= 5) {
        //        categoryMovies.put(el, filmService.getMoviesByGenre("447f42fa4bc5d5ebd07b387dee8385d7",el, page = 2))
        //        i += 1
        //    }
        //}
        //
        //categoryMovies.forEach { (key, value) ->
        //    println(value)
        //}


        val allMovies = mutableListOf<Movie>()
        var currentPage = 1

        do {
            val response = filmService.getMoviesByGenre("447f42fa4bc5d5ebd07b387dee8385d7" , genreId = 28, page = currentPage) // Chiama l'API
            allMovies.addAll(response.results) // Aggiungi i film alla lista totale
            currentPage++ // Passa alla pagina successiva
        } while (currentPage <= 10)


        println(allMovies)




    }



}

suspend fun organizeByCategory(id: List<Int>) {
    var movieByCategories = mutableMapOf<Int, List<Movie>>()
    id.forEach { el ->
        val allMovies = mutableListOf<Movie>()
        var currentPage = 1


        do {
            val response = filmService.getMoviesByGenre( "447f42fa4bc5d5ebd07b387dee8385d7", genreId = el, page = currentPage)
            allMovies.addAll(response.results)
            ++currentPage
        } while (currentPage <= 2)

        val finalList = allMovies.toSet().toList()
        movieByCategories[el] = finalList

    }

    println(movieByCategories.get(12))
}

suspend fun main() {
    organizeByCategory(mutableListOf(12))
}