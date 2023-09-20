package com.esteban.rickandmortyapp.models

import java.io.Serializable

data class CharactersListResponse(
    val info: Info,
    val results: MutableList<RickAndMortyCharacter>
): Serializable