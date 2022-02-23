package com.codeop.recap.viewmodel

import androidx.lifecycle.*
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.repositories.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _favorites: MutableLiveData<List<ComicResponse>> = MutableLiveData(emptyList())
    val favorites: LiveData<List<ComicResponse>> = _favorites

    fun removeComicAsFavorite(comic: ComicResponse) = viewModelScope.launch(Dispatchers.IO) {
        favoritesRepository.removeComicAsFavorite(comic)
        getFavorites()
    }

    fun getFavorites() = viewModelScope.launch(Dispatchers.IO) {
        val result = favoritesRepository.getAllFavorites()

        withContext(Dispatchers.Main) {
            _favorites.value = result
        }
    }
}

class FavoritesViewModelFactory(
    private val favoritesRepository: FavoritesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(FavoritesRepository::class.java)
            .newInstance(favoritesRepository)
    }
}