package com.example.capstone_made.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.core.data.Resource
import com.example.core.domain.model.Games
import com.example.core.domain.usecase.GamesUseCase
import com.example.core.utils.DataDummy
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    private lateinit var searchViewModel: SearchViewModel

    @Mock
    private lateinit var gamesUseCase: GamesUseCase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var observer: Observer<Resource<List<Games>>>

    private lateinit var flow: Flow<Resource<List<Games>>>

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        flow = flow {
            emit(Resource.Loading(listOf()))
            delay(10)
            emit(Resource.Success(DataDummy.generateGameList().toList()))
        }
        `when`(gamesUseCase.getResultSearchGames("FIFA")).thenReturn(flow)
        searchViewModel = SearchViewModel(gamesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `get result from search`() = runTest {
        searchViewModel.searchGames("FIFA").observeForever(observer)
        verify(gamesUseCase, atLeastOnce()).getResultSearchGames("FIFA")

    }

}