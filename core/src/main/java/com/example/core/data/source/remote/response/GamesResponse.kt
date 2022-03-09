package com.example.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class GamesResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("background_image")
    val image: String,

    @field:SerializedName("rating")
    val rating: Double,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("genres")
    val genres: List<Genres>

)
