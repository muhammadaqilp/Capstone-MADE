package com.example.core.domain.usecase

import com.example.core.data.Resource
import com.example.core.domain.model.Games
import com.example.core.domain.repository.IGamesRepository
import kotlinx.coroutines.flow.Flow

class GamesInteractor(private val gamesRepository: IGamesRepository) : GamesUseCase {
    override fun getAllGames(): Flow<Resource<List<Games>>> = gamesRepository.getAllGames()

    override fun getResultSearchGames(query: String): Flow<Resource<List<Games>>> =
        gamesRepository.getResultSearchGames(query)

    override fun getDetailGames(id: Int): Flow<Resource<Games>> = gamesRepository.getDetailGames(id)

    override fun getFavoriteGames(): Flow<List<Games>> = gamesRepository.getFavoriteGames()

    override fun setFavoriteGames(games: Games, state: Boolean) =
        gamesRepository.setFavoriteGames(games, state)

    override fun getDetailGamesFromSearch(id: Int): Flow<Resource<Games>> =
        gamesRepository.getDetailGamesFromSearch(id)

    override suspend fun insertDataGames(games: Games, state: Boolean) =
        gamesRepository.insertDataGames(games, state)
}