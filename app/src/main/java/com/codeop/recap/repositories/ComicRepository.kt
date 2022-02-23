package com.codeop.recap.repositories

import android.content.Context
import com.codeop.recap.api.Retrofit
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.db.DatabaseSingleton

class ComicRepository private constructor(context: Context) {
    companion object {
        private var instance: ComicRepository? = null

        fun getInstance(context: Context): ComicRepository {
            return instance ?: run {
                ComicRepository(context).also { instance = it }
            }
        }
    }

    private val comicsDB = DatabaseSingleton.getInstance(context)

    suspend fun getNewestComic(): ComicResponse? =
        Retrofit.xkcdService.getNewestComic().execute().body()
            ?.also {
                if (!comicsDB.comicDao().isSaved(it.num)) {
                    comicsDB.comicDao().addComic(it)
                }
            }

    suspend fun getComic(number: Int): ComicResponse? = comicsDB.comicDao().getComic(number)
        ?: Retrofit.xkcdService.getSpecificComic(number).execute().body()
            ?.also { comicsDB.comicDao().addComic(it) }

}