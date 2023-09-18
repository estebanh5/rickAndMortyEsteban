package com.esteban.rickandmortyapp.repository

import com.esteban.rickandmortyapp.api.RetrofitInstance

class CharactersRepository {


    suspend fun getCharacters(pageNumber: Int) = RetrofitInstance.api.getCharacters(pageNumber)

    suspend fun searchCharacters(searchQuery: String, pageNumber: Int) = RetrofitInstance.api.searchCharacters(searchQuery, pageNumber)


}