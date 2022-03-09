package com.example.core.utils

import com.example.core.data.source.local.entity.GamesEntity
import com.example.core.data.source.remote.response.GamesResponse
import com.example.core.domain.model.Games
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object DataMapper {
    fun mapResponsesToEntities(input: List<GamesResponse>): List<GamesEntity> {
        val gamesList = ArrayList<GamesEntity>()
        input.map {
            var genre = ""
            for (i in it.genres.indices) {
                val itemName = it.genres[i].name
                genre = if (i == it.genres.lastIndex) {
                    "$genre $itemName"
                } else "$genre $itemName,"
            }
            val games = GamesEntity(
                id = it.id,
                name = it.name,
                image = it.image,
                rating = it.rating,
                description = it.description,
                genre = genre,
                isFavorite = false
            )
            gamesList.add(games)
        }
        return gamesList
    }

    fun mapResponsesToEntities(input: GamesResponse): GamesEntity {
        var genre = ""
        for (i in input.genres.indices) {
            val itemName = input.genres[i].name
            genre = if (i == input.genres.lastIndex) {
                "$genre $itemName"
            } else "$genre $itemName,"
        }
        return GamesEntity(
            id = input.id,
            name = input.name,
            image = input.image,
            rating = input.rating,
            description = input.description,
            genre = genre,
            isFavorite = false
        )
    }

    fun mapResponsesToDomain(input: List<GamesResponse>): Flow<List<Games>> {
        val gamesList = ArrayList<Games>()
        input.map {
            var genre = ""
            for (i in it.genres.indices) {
                val itemName = it.genres[i].name
                genre = if (i == it.genres.lastIndex) {
                    "$genre $itemName"
                } else "$genre $itemName,"
            }
            val games = Games(
                id = it.id,
                name = it.name,
                image = it.image,
                rating = it.rating,
                description = it.description,
                genres = genre,
                isFavorite = false
            )
            gamesList.add(games)
        }
        return flowOf(gamesList)
    }

    fun mapResponsesToDomain(input: GamesResponse): Flow<Games> {
        var genre = ""
        for (i in input.genres.indices) {
            val itemName = input.genres[i].name
            genre = if (i == input.genres.lastIndex) {
                "$genre $itemName"
            } else "$genre $itemName,"
        }
        val games = Games(
            id = input.id,
            name = input.name,
            image = input.image,
            rating = input.rating,
            description = input.description,
            genres = genre,
            isFavorite = false
        )

        return flowOf(games)
    }

    fun mapEntitiesToDomain(input: List<GamesEntity>?): List<Games> =
        input?.map {
            Games(
                id = it.id,
                name = it.name,
                image = it.image,
                rating = it.rating,
                description = it.description,
                genres = it.genre,
                isFavorite = it.isFavorite
            )
        }!!

    fun mapEntitiesToDomain(input: GamesEntity?) =
        Games(
            id = input?.id,
            name = input?.name,
            image = input?.image,
            rating = input?.rating,
            description = input?.description,
            genres = input?.genre,
            isFavorite = input?.isFavorite
        )

    fun mapDomainToEntity(input: Games) = GamesEntity(
        id = input.id,
        name = input.name,
        image = input.image,
        rating = input.rating,
        description = input.description,
        genre = input.genres,
        isFavorite = input.isFavorite
    )
}