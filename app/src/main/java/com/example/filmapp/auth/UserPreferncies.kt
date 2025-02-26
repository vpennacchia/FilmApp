package com.example.filmapp.auth

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {
    private const val PREFS_NAME = "user_prefs"
    private const val EMAIL_KEY = "email"
    private const val PASSWORD_KEY = "password"

    fun saveCredentials(context: Context, email: String, password: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString(EMAIL_KEY, email)
            putString(PASSWORD_KEY, password)
            apply()
        }
    }

    fun getSavedEmail(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(EMAIL_KEY, "") ?: ""
    }

    fun getSavedPassword(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PASSWORD_KEY, "") ?: ""
    }

    fun clearCredentials(context: Context) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove(EMAIL_KEY)
            remove(PASSWORD_KEY)
            apply()
        }
    }

}