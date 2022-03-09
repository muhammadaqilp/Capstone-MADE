package com.example.core.domain.repository

import com.example.core.data.Resource
import com.example.core.domain.model.Games
import kotlinx.coroutines.flow.Flow

interface IGamesRepository {

    fun getAllGames(): Flow<Resource<List<Games>>>

    fun getResultSearchGames(query: String): Flow<Resource<List<Games>>>

    fun getDetailGames(id: Int): Flow<Resource<Games>>

    fun getFavoriteGames(): Flow<List<Games>>

    fun setFavoriteGames(games: Games, state: Boolean)

    fun getDetailGamesFromSearch(id: Int): Flow<Resource<Games>>

    suspend fun insertDataGames(games: Games, state: Boolean)

}