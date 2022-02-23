package com.codeop.recap.repositories

import android.content.Context
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.db.DatabaseSingleton

class FavoritesRepository private constructor(context: Context) {
    companion object {
        private var instance: FavoritesRepository? = null

        fun getInstance(context: Context): FavoritesRepository {
            return instance ?: run {
                FavoritesRepository(context).also { instance = it }
            }
        }
    }

    private val comicsDB = DatabaseSingleton.getInstance(context)

    suspend fun isComicFavorite(comic: ComicResponse): Boolean =
        comicsDB.favoriteDao().isFavorite(comic.asFavorite().id)

    suspend fun addComicAsFavorite(comic: ComicResponse) {
        comicsDB.favoriteDao().addFavorite(comic.asFavorite())
    }

    suspend fun removeComicAsFavorite(comic: ComicResponse) {
        comicsDB.favoriteDao().removeFavorite(comic.asFavorite())
    }

    suspend fun getAllFavorites(): List<ComicResponse> {
        return comicsDB.favoriteDao().getFavorites()
    }
}