package com.example.core.domain.model

data class Games(
    val id: Int?,
    val name: String?,
    var image: String? = null,
    val rating: Double?,
    var description: String? = null,
    val genres: String?,
    val isFavorite: Boolean?
)