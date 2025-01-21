package com.example.filmapp.navigation

import androidx.annotation.DrawableRes
import com.example.filmapp.navigation.Screen.MovieScreen

sealed class Screen(val route: String, val name: String) {


    sealed class MovieScreen(val dTitle: String, val dRoute: String)
        : Screen(dTitle, dRoute) {
        object Home: MovieScreen(
            "Home",
            "homescreen"
        )

        object Details: MovieScreen(
            "Details",
            "detailscreen"
        )

        object Favorites: MovieScreen(
            "Favorites",
            "favoritescreen"
        )

    }


}

val screensInDrawer = listOf(
    MovieScreen.Home,
    MovieScreen.Details,
    MovieScreen.Favorites
)


