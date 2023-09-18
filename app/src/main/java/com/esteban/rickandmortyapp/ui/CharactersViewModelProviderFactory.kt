package com.esteban.rickandmortyapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.esteban.rickandmortyapp.repository.CharactersRepository
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel

class CharactersViewModelProviderFactory(val app: Application, private val newsRepository: CharactersRepository) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CharactersViewModel(app, newsRepository) as T;
    }
}