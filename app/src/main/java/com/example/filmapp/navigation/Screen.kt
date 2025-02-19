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

        object MyAccount: MovieScreen(
            "My Account",
            "accountscreen"
        )

        object Login: MovieScreen(
            "Login",
            "loginscreen"
        )

        object SignUp: MovieScreen(
            "My List",
            "signupscreen"
        )

    }


}

val screensInDrawer = listOf(
    MovieScreen.Home,
    MovieScreen.Details,
    MovieScreen.MyAccount
)


