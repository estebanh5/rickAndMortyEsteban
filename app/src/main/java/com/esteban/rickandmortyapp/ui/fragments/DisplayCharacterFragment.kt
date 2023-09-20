package com.esteban.rickandmortyapp.ui.fragments

import android.app.SearchManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.models.RickAndMortyCharacter
import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import kotlinx.android.synthetic.main.fragment_display_character.*
import kotlinx.android.synthetic.main.item_character_preview.view.*


class DisplayCharacterFragment : Fragment(R.layout.fragment_display_character) {


    private lateinit var viewModel : CharactersViewModel
    private var character : RickAndMortyCharacter? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = (activity as CharactersActivity).viewModel
        character = arguments?.getSerializable("character", RickAndMortyCharacter::class.java)

        Glide.with(this).load(character?.image).into(ivCharacterImage)
        tvName.text = character?.name
        tvSpecies.text = character?.species
        tvGender.text = character?.gender
        tvStatus.text = character?.status

        chipOrigin.text = character?.origin?.name
        chipLocation.text = character?.location?.name

        if(character?.type?.isNotEmpty()!!) {
            chipType.text = character?.type
        }else {
            chipType.visibility = View.INVISIBLE
        }


        (activity as CharactersActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as CharactersActivity).supportActionBar?.title = getString(R.string.details)


        btSearch.setOnClickListener {
            searchOnGoogle()
        }

    }


    private fun searchOnGoogle() {
        val intent = Intent(Intent.ACTION_WEB_SEARCH)
        intent.putExtra(SearchManager.QUERY, character?.name + " " + getString(R.string.rick_and_morty))

        activity?.startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

}