package com.esteban.rickandmortyapp.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.esteban.rickandmortyapp.R
import com.esteban.rickandmortyapp.models.RickAndMortyCharacter
import com.esteban.rickandmortyapp.ui.CharactersActivity
import com.esteban.rickandmortyapp.ui.viewmodel.CharactersViewModel
import kotlinx.android.synthetic.main.fragment_display_character.*
import kotlinx.android.synthetic.main.item_character_preview.view.*

class DisplayCharacterFragment : Fragment(R.layout.fragment_display_character) {


    private lateinit var viewModel : CharactersViewModel


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = (activity as CharactersActivity).viewModel
        val character = arguments?.getSerializable("character", RickAndMortyCharacter::class.java)

        Glide.with(this).load(character?.image).into(ivCharacterImage)
        tvName.text = character?.name
        tvSpecies.text = character?.species
        tvGender.text = character?.gender
        tvStatus.text = character?.status



    }


}