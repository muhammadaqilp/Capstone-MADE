package com.example.capstone_made.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.core.domain.usecase.GamesUseCase

class SearchViewModel(private val gamesUseCase: GamesUseCase) : ViewModel() {

    fun searchGames(query: String) = gamesUseCase.getResultSearchGames(query).asLiveData()

}