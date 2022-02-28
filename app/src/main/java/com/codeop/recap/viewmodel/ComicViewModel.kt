package com.codeop.recap.viewmodel

import androidx.lifecycle.*
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.repositories.ComicRepository
import com.codeop.recap.repositories.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.random.Random

class ComicViewModel(
    private val comicRepository: ComicRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel(), KoinComponent {
    private var comicLimit: Int = 0
    private var comicNumber: Int = 0

    private val _currentComic: MutableLiveData<ComicResponse?> = MutableLiveData(null)
    val currentComic: LiveData<ComicResponse?> = _currentComic

    private val _isNextActive: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNextActive: LiveData<Boolean> = _isNextActive

    private val _isCurrentFavorite: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCurrentFavorite: LiveData<Boolean> = _isCurrentFavorite

    init {
        if (_currentComic.value == null) getNewestComic()
    }

    private fun setNextActiveState() {
        _isNextActive.value = comicLimit != comicNumber
    }

    private fun setFavoriteState() = viewModelScope.launch(Dispatchers.IO) {
        val currentComic = currentComic.value ?: return@launch
        val result = favoritesRepository.isComicFavorite(currentComic)

        withContext(Dispatchers.Main) {
            _isCurrentFavorite.value = result
        }
    }

    fun switchFavoriteState() = viewModelScope.launch(Dispatchers.IO) {
        val isFavorite = isCurrentFavorite.value ?: return@launch
        val current = currentComic.value ?: return@launch

        if (isFavorite) {
            favoritesRepository.removeComicAsFavorite(current)
        } else {
            favoritesRepository.addComicAsFavorite(current)
        }

        withContext(Dispatchers.Main) {
            setFavoriteState()
        }
    }

    private fun setVariables(comicResponse: ComicResponse) {
        _currentComic.value = comicResponse
        setNextActiveState()
        setFavoriteState()
    }

    private fun getNewestComic() = viewModelScope.launch(Dispatchers.IO) {
        val result = comicRepository.getNewestComic()

        withContext(Dispatchers.Main) {
            comicLimit = result?.num ?: 0
            comicNumber = result?.num ?: 0

            result?.let { setVariables(it) }
        }
    }

    fun getNextComic() = viewModelScope.launch(Dispatchers.IO) {
        val number = ((comicNumber + 1)
            .takeIf { it < comicLimit } ?: comicLimit)
            .also { comicNumber = it }

        val result = comicRepository.getComic(number)

        withContext(Dispatchers.Main) {
            result?.let { setVariables(it) }
        }
    }


    fun getPreviousComic() = viewModelScope.launch(Dispatchers.IO) {
        val number = ((comicNumber - 1)
            .takeIf { it > 0 } ?: 1)
            .also { comicNumber = it }

        val result = comicRepository.getComic(number)

        withContext(Dispatchers.Main) {
            result?.let { setVariables(it) }
        }
    }

    fun getRandomComic() = viewModelScope.launch(Dispatchers.IO) {
        val number = Random.nextInt(comicLimit)
        val result = comicRepository.getComic(number)

        withContext(Dispatchers.Main) {
            result?.let { setVariables(it) }
        }
    }
}