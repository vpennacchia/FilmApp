package com.example.filmapp

sealed class Screen(val route: String) {

    object MovieScreen: Screen("moviescreen")
    object DetailScreen: Screen("detailscreen")

}