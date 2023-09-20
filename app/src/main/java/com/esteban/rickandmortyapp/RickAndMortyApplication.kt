package com.esteban.rickandmortyapp

import android.app.Application
import com.esteban.rickandmortyapp.dagger.DaggerRickMortyComponent
import com.esteban.rickandmortyapp.dagger.RickMortyComponent


class RickAndMortyApplication : Application() {

    lateinit var rickMortyComponent: RickMortyComponent

    override fun onCreate() {
        super.onCreate()
        initDaggerComponent()
    }
    private fun initDaggerComponent() {
        rickMortyComponent = DaggerRickMortyComponent.builder().build()
    }
}