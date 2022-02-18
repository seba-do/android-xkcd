package com.codeop.recap.repositories

import android.content.Context
import androidx.room.Room
import com.codeop.recap.api.Retrofit
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.db.AppDatabase
import com.codeop.recap.db.DatabaseSingleton
import kotlin.random.Random

class ComicRepository private constructor(context: Context) {
    companion object {
        private var instance: ComicRepository? = null

        fun getInstance(context: Context): ComicRepository {
            return instance ?: run {
                ComicRepository(context).also { instance = it }
            }
        }
    }

    var comicLimit: Int = 0
    var comicNumber: Int = 0

    private val comicsDB = DatabaseSingleton.getInstance(context)

    suspend fun getNewestComic(): ComicResponse? {
        return Retrofit.xkcdService.getNewestComic().execute().body()
            ?.also {
                comicLimit = it.num
                comicNumber = it.num

                if(!comicsDB.comicDao().isSaved(it.num)) {
                    comicsDB.comicDao().addComic(it)
                }
            }
    }

    suspend fun restoreLastComic(): ComicResponse? {
        return if (comicNumber == 0)
            getNewestComic()
        else
            comicsDB.comicDao().getComic(comicNumber)
                ?: Retrofit.xkcdService.getSpecificComic(comicNumber).execute().body()
                    ?.also {
                        comicNumber = it.num
                        comicsDB.comicDao().addComic(it)
                    }
    }

    suspend fun getRandomComic(): ComicResponse? {
        val number = Random.nextInt(comicLimit)

        return comicsDB.comicDao().getComic(number)
            ?: Retrofit.xkcdService.getSpecificComic(number).execute().body()
                ?.also {
                    comicNumber = it.num
                    comicsDB.comicDao().addComic(it)
                }
    }

    suspend fun getNextComic(): ComicResponse? {
        val number = ++comicNumber

        return comicsDB.comicDao().getComic(number)
            ?: Retrofit.xkcdService.getSpecificComic(number).execute().body()
                ?.also {
                    comicNumber = it.num
                    comicsDB.comicDao().addComic(it)
                }
    }

    suspend fun getPreviousComic(): ComicResponse? {
        val number = --comicNumber

        return comicsDB.comicDao().getComic(number)
            ?: Retrofit.xkcdService.getSpecificComic(number).execute().body()
                ?.also {
                    comicNumber = it.num
                    comicsDB.comicDao().addComic(it)
                }
    }
}