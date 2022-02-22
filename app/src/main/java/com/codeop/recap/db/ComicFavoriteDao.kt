package com.codeop.recap.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.codeop.recap.data.ComicFavorite
import com.codeop.recap.data.ComicResponse

@Dao
interface ComicFavoriteDao {
    @Insert
    suspend fun addFavorite(favorite: ComicFavorite)

    @Delete
    suspend fun removeFavorite(favorite: ComicFavorite)

    @Query("SELECT * FROM ComicFavorite AS favorite " +
            "INNER JOIN ComicResponse AS comic ON comic.num = favorite.id")
    suspend fun getFavorites(): List<ComicResponse>

    @Query("SELECT EXISTS (SELECT 1 FROM ComicFavorite WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}