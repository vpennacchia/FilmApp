package com.example.filmapp.navigation

sealed class Screen(val route: String) {

    object MovieScreen: Screen("moviescreen")
    object DetailScreen: Screen("detailscreen")

}