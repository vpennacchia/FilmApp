package com.example.filmapp

import android.app.Application
//Application() ci permette di inizializzare uno "stato globale" in questo caso il nostro db, utile in tutta l'app
//l'application infatti tramite il metodo onCreate e specificando la classe nel manifest.xml verrà instanziata prima di ogni altra classe
class MovieApp: Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this) //wishlist app è il context in cui si deve inizializzare il db
    }
}