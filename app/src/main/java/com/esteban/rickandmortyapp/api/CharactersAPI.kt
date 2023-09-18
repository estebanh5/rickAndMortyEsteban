package com.esteban.rickandmortyapp.api

import com.esteban.rickandmortyapp.models.CharactersListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharactersAPI {


    @GET("/api/character")
    suspend fun getCharacters(@Query("page") pageNumber: Int = 1) : Response<CharactersListResponse>

    @GET("/api/character")
    suspend fun searchCharacters(@Query("name") query: String, @Query("page") pageNumber: Int = 1) : Response<CharactersListResponse>

}