package com.esteban.rickandmortyapp.dagger


import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.fragments.CharactersListFragment
import com.esteban.rickandmortyapp.ui.fragments.SearchCharactersFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface RickMortyComponent {


    fun inject(main: CharactersActivity?)

    fun inject(main: CharactersListFragment)

    fun inject(main: SearchCharactersFragment)
}