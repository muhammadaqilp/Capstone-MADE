package com.example.core.utils

import com.example.core.data.source.local.entity.GamesEntity
import com.example.core.data.source.remote.response.GamesResponse
import com.example.core.data.source.remote.response.Genres
import com.example.core.domain.model.Games

object DataDummy {

    fun generateGameList(): List<Games> {
        val list = mutableListOf<Games>()
        for (i in 0..9) {
            list.add(
                Games(
                    i,
                    "Games $i",
                    "Image $i",
                    i.toDouble(),
                    "Lorem Ipsum $i",
                    "Adventure, Science",
                    false
                )
            )
        }
        return list
    }

    fun generateGameEntities(): List<GamesEntity> {
        val list = mutableListOf<GamesEntity>()
        for (i in 0..9) {
            list.add(
                GamesEntity(
                    i,
                    "Games $i",
                    "Image $i",
                    i.toDouble(),
                    "Lorem Ipsum $i",
                    "Adventure, Science",
                    false
                )
            )
        }
        return list
    }

    fun generateGameResponse(): List<GamesResponse> {
        val list = mutableListOf<GamesResponse>()
        for (i in 0..9) {
            list.add(
                GamesResponse(
                    i,
                    "Games $i",
                    "Image $i",
                    i.toDouble(),
                    "Lorem Ipsum $i",
                    listOf(Genres(name = "Adventure", id = 1)),
                )
            )
        }
        return list
    }

}