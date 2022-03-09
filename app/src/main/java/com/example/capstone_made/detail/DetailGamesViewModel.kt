package com.example.capstone_made.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.model.Games
import com.example.core.domain.usecase.GamesUseCase

class DetailGamesViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    fun getDetailGames(id: Int) = gamesUseCase.getDetailGames(id).asLiveData()

    fun getDetailGamesFromSearch(id: Int) = gamesUseCase.getDetailGamesFromSearch(id).asLiveData()

    fun setFavoriteGames(games: Games, newState: Boolean) =
        gamesUseCase.setFavoriteGames(games, newState)

    suspend fun insertToDB(games: Games, newState: Boolean) =
        gamesUseCase.insertDataGames(games, newState)
}