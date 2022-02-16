package com.codeop.recap.repositories

import com.codeop.recap.api.Retrofit
import com.codeop.recap.data.ComicResponse
import kotlin.random.Random

object ComicRepository {

    var comicLimit: Int = 0
    var comicNumber: Int = 0

    fun getNewestComic(): ComicResponse? {
        return Retrofit.xkcdService.getNewestComic().execute().body()?.also {
            comicLimit = it.num
            comicNumber = it.num
        }
    }

    fun getRandomComic(): ComicResponse? {
        return Retrofit.xkcdService.getSpecificComic(Random.nextInt(comicLimit)).execute()
            .body().also { comicNumber = it?.num ?: comicLimit }
    }

    fun getNextComic(): ComicResponse? {
        return Retrofit.xkcdService.getSpecificComic(++comicNumber).execute().body()
            .also { comicNumber = it?.num ?: comicLimit }
    }

    fun getPreviousComic(): ComicResponse? {
        return Retrofit.xkcdService.getSpecificComic(--comicNumber).execute().body()
            .also { comicNumber = it?.num ?: comicLimit }
    }
}