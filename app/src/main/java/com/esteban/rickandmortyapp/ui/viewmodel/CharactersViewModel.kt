package com.esteban.rickandmortyapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.esteban.rickandmortyapp.RickAndMortyApplication
import com.esteban.rickandmortyapp.models.CharactersListResponse
import com.esteban.rickandmortyapp.repository.CharactersRepository
import com.esteban.rickandmortyapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class CharactersViewModel(app: Application, private val charactersRepository: CharactersRepository) : AndroidViewModel(app) {

    val characters: MutableLiveData<Resource<CharactersListResponse>> = MutableLiveData()
    var charactersPage = 1
    var charactersResponse : CharactersListResponse? = null

    val searchCharacters: MutableLiveData<Resource<CharactersListResponse>> = MutableLiveData()
    var searchCharactersPage = 1
    var searchCharactersResponse: CharactersListResponse? = null


    init {
        getCharacters()
    }


    fun getCharacters() = viewModelScope.launch {
        safeCharactersCall()
    }

    fun searchCharacters(searchQuery: String) = viewModelScope.launch {
        safeSearchCharacters(searchQuery)
    }



    private suspend fun safeCharactersCall() {
        characters.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = charactersRepository.getCharacters(charactersPage)
                characters.postValue(handleCharactersResponse(response))
            }else {
                characters.postValue(Resource.Error("No internet connection"))
            }

        }catch(t: Throwable) {
            when(t) {
                is IOException -> characters.postValue(Resource.Error("Network Failure"))
                else -> characters.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private suspend fun safeSearchCharacters(searchQuery: String) {
        searchCharacters.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = charactersRepository.searchCharacters(searchQuery, searchCharactersPage)
                searchCharacters.postValue(handleSearchCharactersResponse(response))
            }else {
                searchCharacters.postValue(Resource.Error("No internet connection"))
            }

        }catch(t: Throwable) {
            when(t) {
                is IOException -> searchCharacters.postValue(Resource.Error("Network Failure"))
                else -> searchCharacters.postValue(Resource.Error("Conversion Error"))
            }

        }
    }


    private fun handleCharactersResponse(response: Response<CharactersListResponse>) : Resource<CharactersListResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                charactersPage++
                if(charactersResponse == null) {
                    charactersResponse = resultResponse
                }else {
                    val oldCharacters = charactersResponse?.results
                    val newCharacters = resultResponse.results

                    oldCharacters?.addAll(newCharacters)

                }
                return Resource.Success(charactersResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    private fun handleSearchCharactersResponse(response: Response<CharactersListResponse>) : Resource<CharactersListResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchCharactersPage++
                if(searchCharactersResponse == null) {
                    searchCharactersResponse = resultResponse
                }else {
                    val oldCharacters = searchCharactersResponse?.results
                    val newCharacters = resultResponse.results

                    oldCharacters?.addAll(newCharacters)

                }
                return Resource.Success(searchCharactersResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }




    private fun hasInternetConnection() : Boolean {
        val connectivityManager = getApplication<RickAndMortyApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }

        return false

    }

}