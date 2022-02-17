package com.codeop.recap.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ComicResponse(
    val num: Int,
    val link: String,
    val news: String,
    val transcript: String,
    val title: String,
    val safe_title: String,
    val img: String,
    val alt: String,
    val day: String,
    val month: String,
    val year: String,
): Parcelable
