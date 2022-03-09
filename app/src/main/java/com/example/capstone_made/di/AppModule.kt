package com.example.capstone_made.di

import com.example.capstone_made.detail.DetailGamesViewModel
import com.example.capstone_made.home.HomeViewModel
import com.example.capstone_made.search.SearchViewModel
import com.example.core.domain.usecase.GamesInteractor
import com.example.core.domain.usecase.GamesUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<GamesUseCase> { GamesInteractor(get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailGamesViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}