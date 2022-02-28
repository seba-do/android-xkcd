package com.codeop.recap.repositories

import com.codeop.recap.data.ComicResponse
import com.codeop.recap.db.DBConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavoritesRepository : KoinComponent {

    private val comicsDB by inject<DBConnection>()

    suspend fun isComicFavorite(comic: ComicResponse): Boolean =
        comicsDB.instance.favoriteDao().isFavorite(comic.asFavorite().id)

    suspend fun addComicAsFavorite(comic: ComicResponse) {
        comicsDB.instance.favoriteDao().addFavorite(comic.asFavorite())
    }

    suspend fun removeComicAsFavorite(comic: ComicResponse) {
        comicsDB.instance.favoriteDao().removeFavorite(comic.asFavorite())
    }

    suspend fun getAllFavorites(): List<ComicResponse> {
        return comicsDB.instance.favoriteDao().getFavorites()
    }
}