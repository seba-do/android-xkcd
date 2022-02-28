package com.codeop.recap.repositories

import com.codeop.recap.api.Retrofit
import com.codeop.recap.data.ComicResponse
import com.codeop.recap.db.DBConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ComicRepository : KoinComponent {

    private val comicsDB: DBConnection by inject()

    suspend fun getNewestComic(): ComicResponse? =
        Retrofit.xkcdService.getNewestComic().execute().body()
            ?.also {
                if (!comicsDB.instance.comicDao().isSaved(it.num)) {
                    comicsDB.instance.comicDao().addComic(it)
                }
            }

    suspend fun getComic(number: Int): ComicResponse? =
        comicsDB.instance.comicDao().getComic(number)
            ?: Retrofit.xkcdService.getSpecificComic(number).execute().body()
                ?.also { comicsDB.instance.comicDao().addComic(it) }

}