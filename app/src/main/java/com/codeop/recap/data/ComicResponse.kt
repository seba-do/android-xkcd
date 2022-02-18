package com.codeop.recap.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ComicResponse(
    @PrimaryKey val num: Int,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "news") val news: String,
    @ColumnInfo(name = "transcript") val transcript: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "safe_title") val safe_title: String,
    @ColumnInfo(name = "img") val img: String,
    @ColumnInfo(name = "alt") val alt: String,
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "month") val month: String,
    @ColumnInfo(name = "year") val year: String,
) : Parcelable {
    fun asFavorite() = ComicFavorite(num)
}
