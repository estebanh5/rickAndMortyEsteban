package com.esteban.rickandmortyapp.models

data class CharactersListResponse(
    val info: Info,
    val results: MutableList<RickAndMortyCharacter>
)