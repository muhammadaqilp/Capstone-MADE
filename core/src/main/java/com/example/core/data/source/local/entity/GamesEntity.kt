package com.example.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GamesEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "image")
    var image: String? = null,

    @ColumnInfo(name = "rating")
    var rating: Double?,

    @ColumnInfo(name = "description")
    var description: String? = null,

    @ColumnInfo(name = "genre")
    var genre: String?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean?
)
