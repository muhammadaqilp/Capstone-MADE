package com.example.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GamesUseCase

class FavoriteViewModel(gamesUseCase: GamesUseCase) : ViewModel() {

    val favorites = gamesUseCase.getFavoriteGames().asLiveData()

}