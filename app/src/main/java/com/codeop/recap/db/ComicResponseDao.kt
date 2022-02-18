package com.codeop.recap.db

import androidx.room.*
import com.codeop.recap.data.ComicResponse

@Dao
interface ComicResponseDao {
    @Query("SELECT * FROM ComicResponse")
    suspend fun getAll(): List<ComicResponse>

    @Query("SELECT * FROM ComicResponse WHERE num LIKE :num")
    suspend fun getComic(num: Int): ComicResponse?

    @Query("SELECT EXISTS (SELECT 1 FROM ComicResponse WHERE num = :num)")
    suspend fun isSaved(num: Int): Boolean

    @Insert
    suspend fun addComic(comic: ComicResponse)

    @Delete
    suspend fun removeComic(comic: ComicResponse)
}