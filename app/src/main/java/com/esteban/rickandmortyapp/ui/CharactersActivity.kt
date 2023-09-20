package com.esteban.rickandmortyapp.ui


import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.RickAndMortyApplication
import com.esteban.rickandmortyapp.dagger.DaggerRickMortyComponent
import com.esteban.rickandmortyapp.repository.CharactersRepository
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import javax.inject.Inject


class CharactersActivity : AppCompatActivity() {


    lateinit var viewModel : CharactersViewModel

    @Inject lateinit var repository: CharactersRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        (application as RickAndMortyApplication).rickMortyComponent.inject(this)


        val viewModelProviderFactory = CharactersViewModelProviderFactory(application, repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[CharactersViewModel::class.java]
        setContentView(R.layout.activity_main)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }





}