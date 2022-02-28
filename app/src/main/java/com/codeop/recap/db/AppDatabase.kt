package com.codeop.recap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codeop.recap.data.ComicFavorite
import com.codeop.recap.data.ComicResponse

@Database(entities = [ComicResponse::class, ComicFavorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicResponseDao
    abstract fun favoriteDao(): ComicFavoriteDao
}


class DBConnection(context: Context) {
    val instance: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "comics").build()
}