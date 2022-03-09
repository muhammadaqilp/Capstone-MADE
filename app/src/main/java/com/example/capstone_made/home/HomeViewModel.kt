package com.example.capstone_made.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GamesUseCase

class HomeViewModel(gamesUseCase: GamesUseCase) : ViewModel() {

    val games = gamesUseCase.getAllGames().asLiveData()

}