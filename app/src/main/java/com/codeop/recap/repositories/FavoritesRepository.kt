package com.codeop.recap.repositories

import android.content.Context
import com.codeop.recap.api.Retrofit
import com.codeop.recap.data.ComicResponse

class FavoritesRepository private constructor(context: Context) {
    companion object {
        private const val FAVORITES_DB = "favorites-db"
        private var instance: FavoritesRepository? = null

        fun getInstance(context: Context): FavoritesRepository {
            return instance ?: run {
                FavoritesRepository(context).also { instance = it }
            }
        }
    }

    private val persistenceRepository = PersistenceRepository(context, FAVORITES_DB)
    private val favoritesSet: MutableSet<Int> = mutableSetOf()

    init {
        favoritesSet.addAll(persistenceRepository.getAllValues())
    }

    fun isComicFavorite(comic: ComicResponse): Boolean = favoritesSet.contains(comic.num)

    fun addComicAsFavorite(comic: ComicResponse) {
        favoritesSet.add(comic.num)
        persistenceRepository.writeInt(comic.num.toString(), comic.num)
    }

    fun removeComicAsFavorite(comic: ComicResponse) {
        favoritesSet.remove(comic.num)
        persistenceRepository.deleteInt(comic.num.toString())
    }

    fun getAllFavorites() : List<ComicResponse> {
        val result = mutableListOf<ComicResponse>()
        persistenceRepository.getAllValues().forEach {
            Retrofit.xkcdService.getSpecificComic(it).execute().body()?.let { comic ->
                result.add(comic)
            }
        }

        return result
    }
}