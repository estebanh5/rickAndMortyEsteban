package com.esteban.rickandmortyapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.repository.CharactersRepository
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel

class CharactersActivity : AppCompatActivity() {


    lateinit var viewModel : CharactersViewModel

    lateinit var repository: CharactersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = CharactersRepository()
        val viewModelProviderFactory = CharactersViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[CharactersViewModel::class.java]
        setContentView(R.layout.activity_main)

    }
}