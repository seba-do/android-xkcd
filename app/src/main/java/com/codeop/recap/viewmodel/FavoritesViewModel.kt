package com.codeop.recap.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.repositories.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesRepository: FavoritesRepository) : ViewModel() {

    val favorites: MutableLiveData<List<ComicResponse>> = MutableLiveData(emptyList())

    fun removeComicAsFavorite(comic: ComicResponse) = viewModelScope.launch(Dispatchers.IO) {
        favoritesRepository.removeComicAsFavorite(comic)
        getFavorites()
    }

    fun getFavorites() = viewModelScope.launch(Dispatchers.IO) {
        val result = favoritesRepository.getAllFavorites()
        favorites.postValue(result)
    }
}