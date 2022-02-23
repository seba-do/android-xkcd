package com.codeop.recap.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ComicFavorite(
    @PrimaryKey val id: Int
)